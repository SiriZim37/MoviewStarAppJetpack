package com.siri.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.siri.data.model.asLocalModel
import com.siri.data.utils.CacheValidator
import com.siri.database.datasource.MovieLocalDataSource
import com.siri.database.datasource.PreferencesLocalDataSource
import com.siri.database.model.MovieLocalModel
import com.siri.database.model.RemoteKeys
import com.siri.network.datasource.MovieDataSource
import com.siri.network.models.MovieModel
import javax.inject.Inject


/*
MovieRemoteMediator เป็นคลาสที่สืบทอดจาก RemoteMediator ซึ่งทำหน้าที่:
ควบคุมว่าเมื่อไรควรโหลดข้อมูลจาก API
เชื่อมข้อมูลจาก API เข้ากับฐานข้อมูล Room
ตรวจสอบว่าแคชหมดอายุหรือไม่ (ผ่าน CacheValidator)
เก็บสถานะของแต่ละหน้า (ผ่าน RemoteKeys)
ใช้ร่วมกับ Paging 3 เพื่อให้สามารถโหลดข้อมูลทีละหน้าได้อย่างมีประสิทธิภาพ
สรุปสั้น: เป็นสะพานเชื่อมระหว่าง Network → Room → UI ด้วย Paging 3
UI → ViewModel
        ↓
     Repository
        ↓
     Pager<Int, MovieLocalModel>
        ↓
 ┌─────────────┐       ┌────────────┐
 │ Local Room  │ ⇄→→→→ │ MovieRemoteMediator │ ⇄→ API
 └─────────────┘       └────────────┘

วลาที่ Paging library ต้องโหลดหน้าใหม่ เช่น กด scroll ไปเรื่อย ๆ มันต้องรู้ว่า:
จะโหลดหน้า ไหนถัดไป (ใช้ nextKey)
หรือจะย้อนกลับไปโหลดหน้าเก่า (ใช้ prevKey)
หรืออยู่ตรงกลาง (ใช้ getRemoteKeyClosestToCurrentPosition())

🔄 วิธีการทำงานของ RemoteMediator โดยใช้ Key:
REFRESH:
ใช้ getRemoteKeyClosestToCurrentPosition() → เพื่อรู้ว่าเราอยู่ใกล้หน้าไหน
แล้วคำนวณว่า page = nextKey - 1 หรือ 1 ถ้ายังไม่มี key
PREPEND:
ใช้ getRemoteKeyForFirstItem() → เอา prevKey มาดูว่ามีหน้าก่อนหน้ารึเปล่า
ถ้าไม่มี prevKey แปลว่า ถึงต้นสุดของรายการแล้ว
APPEND:
ใช้ getRemoteKeyForLastItem() → เอา nextKey มาดูว่ามีหน้าถัดไปรึเปล่า
ถ้าไม่มี nextKey แปลว่า ถึงท้ายสุดของรายการแล้ว
✅ ประโยชน์
ป้องกันการโหลดข้อมูลซ้ำ
รู้ตำแหน่งและทิศทางการโหลด
เก็บสถานะของการโหลดแต่ละรายการไว้ในฐานข้อมูล (เช่น Room)

	ทำงาน async โหลดข้อมูลจาก API และเขียนลง DB แบบไม่บล็อก thread
 */
@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val remoteDataSource: MovieDataSource, //  ของ Network
    private val localDataSource: MovieLocalDataSource, // -ของ Database
    private val preferencesLocalDataSource: PreferencesLocalDataSource, // DataSource สำหรับจัดการข้อมูลที่เก็บใน SharedPreferences หรือ DataStore (key-value storage บน Android)
    private val cacheValidator: CacheValidator
) : RemoteMediator<Int, MovieLocalModel>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        val isCacheValid = cacheValidator.isValid()
        return if (isCacheValid) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieLocalModel>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            // 1. เรียก API เพื่อนำข้อมูลหนังหน้า `page` มา
            val movies = remoteDataSource.getUpcoming(page).map(MovieModel::asLocalModel)
            // 2. ตรวจสอบว่าเป็นหน้าสุดท้ายของข้อมูลหรือยัง (เช็คว่ารายการว่างไหม)
            val endOfPaginationReached = movies.isEmpty()
            // 3. สร้าง prevKey และ nextKey สำหรับแต่ละรายการ
            val prevKey = if (page == 1) null else page.minus(1)
            // 4. สร้าง RemoteKeys สำหรับใช้บอกตำแหน่งหน้าก่อน/หน้าถัดไปของแต่ละ movie
            val nextKey = if (endOfPaginationReached) null else page.plus(1)
            val keys = movies.map {
                RemoteKeys(
                    movieId = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            // 5. บันทึกข้อมูลทั้งหมดลง Room database แบบ transaction
            localDataSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // รีเซ็ตข้อมูล ถ้าเป็นการ refresh ใหม่หมด
                    preferencesLocalDataSource.setLastFetchTimestamp(System.currentTimeMillis())
                    localDataSource.clearRemoteKeys()
                    localDataSource.clearMovies()
                }
                // บันทึก keys และ movies ลง database
                localDataSource.saveRemoteKeys(keys)
                localDataSource.saveMovies(movies)
            }
            // 6. แจ้งว่าโหลดสำเร็จ และระบุว่าเป็นหน้าสุดท้ายหรือยัง
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            // 7. ถ้ามีข้อผิดพลาด เช่น API ล่ม, DB เขียนไม่ได้, return Error
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieLocalModel>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.lastItemOrNull()?.let { movie ->
            // Get the remote keys of the last item retrieved
            localDataSource.getRemoteKeysByMovieId(movie.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieLocalModel>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.firstItemOrNull()?.let { movie ->
            // Get the remote keys of the first item retrieved
            localDataSource.getRemoteKeysByMovieId(movie.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieLocalModel>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                localDataSource.getRemoteKeysByMovieId(movieId)
            }
        }
    }
}