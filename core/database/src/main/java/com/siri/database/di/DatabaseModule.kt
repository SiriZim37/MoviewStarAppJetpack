package com.siri.database.di

import android.content.Context
import com.siri.database.dao.MovieDao
import com.siri.database.db.MovieDatabase
import com.siri.database.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
ทำไมต้องสร้างทั้งสามตัวนี้?
โมดูล	เหตุผลที่ต้องสร้างและใช้
DataStoreModule	- เพื่อให้มี DataStore สำหรับเก็บข้อมูล settings, flags, timestamp แบบ key-value
- ข้อมูลพวกนี้ไม่เหมาะเก็บในฐานข้อมูล Room
- สร้างเป็น singleton เพราะควรมี instance เดียวทั่วแอป
DatabaseModule	- เพื่อสร้างฐานข้อมูล Room และ DAOs สำหรับเก็บข้อมูลหลักของแอป (เช่น หนัง, remote keys)
- มี DAO ช่วยให้ query และจัดการข้อมูลแบบ SQL ได้สะดวก
- Database ควรเป็น singleton เพื่อประสิทธิภาพและป้องกัน resource ซ้ำซ้อน
LocalDataSourceModule	- เพื่อเชื่อม interface กับ implementation ของ local data sources
- ช่วยให้โค้ดแยกชั้น abstraction ง่ายต่อการทดสอบและเปลี่ยน implementation
- ทำให้ Hilt รู้ว่าจะ inject ตัวไหนเมื่อเรียกใช้ interface
 */
/*
DatabaseModule	สร้างฐานข้อมูล Room และ DAOs
MovieDatabase, MovieDao, RemoteKeysDao
Singleton	object + @Provides
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase = MovieDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: MovieDatabase): RemoteKeysDao = database.remoteKeysDao()
}