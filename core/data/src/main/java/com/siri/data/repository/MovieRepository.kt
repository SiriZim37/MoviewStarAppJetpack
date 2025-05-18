package com.siri.data.repository

import androidx.paging.PagingData
import com.siri.data.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getUpcomingMovies(): Flow<PagingData<Movie>>
    fun getNowplayingMovies(): Flow<PagingData<Movie>>
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getUpcomingMovies(searchQuery: String, category: String): Flow<PagingData<Movie>>
}