package com.siri.network.di

import com.siri.network.datasource.MovieDataSource
import com.siri.network.datasource.MovieDataSourceImpl
import com.siri.network.utils.ExceptionHandler
import com.siri.network.utils.NetworkExceptionHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindMovieRemoteDataSource(
        defaultMovieRemoteDataSource: MovieDataSourceImpl
    ): MovieDataSource

    @Binds
    abstract fun bindMovieExceptionHandler(
        movieExceptionHandler: NetworkExceptionHandler
    ): ExceptionHandler
}