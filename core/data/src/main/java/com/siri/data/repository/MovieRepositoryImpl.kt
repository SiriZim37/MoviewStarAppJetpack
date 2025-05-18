package com.siri.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.siri.data.di.IoDispatcher
import com.siri.data.model.Movie
import com.siri.data.model.asExternalModel
import com.siri.database.model.MovieLocalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val moviePager: Pager<Int, MovieLocalModel>,
) : MovieRepository {

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return moviePager.flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }.flowOn(ioDispatcher)
    }

    override fun getUpcomingMovies(searchQuery: String, category: String): Flow<PagingData<Movie>> {
        return moviePager.flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }.flowOn(ioDispatcher)
    }

    override fun getNowplayingMovies(): Flow<PagingData<Movie>> {
        return moviePager.flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }.flowOn(ioDispatcher)
    }

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return moviePager.flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }.flowOn(ioDispatcher)
    }



}