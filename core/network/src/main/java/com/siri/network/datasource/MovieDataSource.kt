package com.siri.network.datasource

import com.siri.network.models.MovieModel
import com.siri.network.models.MovieResponse
import com.siri.network.models.PopularResponse

interface MovieDataSource  {
    suspend fun getNowPlaying(page: Int): List<MovieModel>
    suspend fun getUpcoming(page: Int): List<MovieModel>
    suspend fun getPopular(page: Int): List<MovieModel>

}
