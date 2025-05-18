package com.siri.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.siri.data.repository.MovieRemoteMediator
import com.siri.data.utils.CacheValidator
import com.siri.database.datasource.MovieLocalDataSource
import com.siri.database.datasource.PreferencesLocalDataSource
import com.siri.database.model.MovieLocalModel
import com.siri.network.datasource.MovieDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/*
PagingModule ist ein Dagger Hilt Modul, das mithilfe von @Provides
die folgenden Paging-Komponenten bereitstellt:
    -เป็น Dagger Hilt Module ที่ใช้ @Provides เพื่อสร้างและ Inject Object ที่เกี่ยวข้องกับการแบ่งหน้า (Paging) เช่น:
PagingConfig – konfiguriert die Seitengröße
    -กำหนดขนาดของแต่ละหน้า
MovieRemoteMediator – verwaltet, wann Daten vom Server geladen werden sollen
    - ควบคุมว่าเมื่อไรควรโหลดข้อมูลจาก API
Pager – kombiniert lokale und entfernte Datenquellen (Remote & Room)
    -เป็นตัวรวมข้อมูลจาก local database และ remote API เข้าด้วยกัน
 */
@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun providePagingConfig() = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
    )

    @Provides
    @Singleton
    fun provideMovieRemoteMediator(
        remoteDataSource: MovieDataSource,
        localDataSource: MovieLocalDataSource,
        preferencesLocalDataSource: PreferencesLocalDataSource,
        cacheValidator: CacheValidator
    ): RemoteMediator<Int, MovieLocalModel> = MovieRemoteMediator(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
        preferencesLocalDataSource = preferencesLocalDataSource,
        cacheValidator = cacheValidator
    )

    @Provides
    @Singleton
    fun provideMoviePager(
        pagingConfig: PagingConfig,
        movieRemoteMediator: MovieRemoteMediator,
        localDataSource: MovieLocalDataSource
    ): Pager<Int, MovieLocalModel> = Pager(
        config = pagingConfig,
        remoteMediator = movieRemoteMediator,
        pagingSourceFactory = {
            localDataSource.pagingSource()
        },
    )
}