package com.siri.data.di

import com.siri.data.repository.MovieRepository
import com.siri.data.repository.MovieRepositoryImpl
import com.siri.data.utils.CacheValidator
import com.siri.data.utils.MovieCacheValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        defaultMovieRepository: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindMovieCacheValidator(
        movieCacheValidator: MovieCacheValidator
    ): CacheValidator
}