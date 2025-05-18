package com.siri.database.di

import com.siri.database.datasource.MovieLocalDataSource
import com.siri.database.datasource.MovieLocalDataSourceImpl
import com.siri.database.datasource.PreferencesLocalDataSource
import com.siri.database.datasource.PreferencesLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*
ผูก interface กับ implementation ของ local data sources ผ่าน Dagger Hilt
ลักษณะ:
เป็นโมดูลแบบ abstract ใช้ @Binds เพื่อบอกว่าเวลาที่แอปเรียกใช้ interface
(MovieLocalDataSource, PreferencesLocalDataSource)
ให้ใช้ implementation ที่ระบุ (MovieLocalDataSourceImpl, PreferencesLocalDataSourceImpl)

 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindMovieLocalDataSource(
        defaultMovieLocalDataSource: MovieLocalDataSourceImpl
    ): MovieLocalDataSource

    @Binds
    abstract fun bindPreferencesLocalDataSource(
        defaultPreferencesLocalDataSource: PreferencesLocalDataSourceImpl
    ): PreferencesLocalDataSource
}