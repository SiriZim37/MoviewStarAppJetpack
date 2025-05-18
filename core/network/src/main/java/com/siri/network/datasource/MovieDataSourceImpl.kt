package com.siri.network.datasource

import com.siri.network.api.TmdbApiService
import com.siri.network.models.MovieModel
import com.siri.network.utils.ExceptionHandler
import javax.inject.Inject

class MovieDataSourceImpl  @Inject constructor(
    private val apiService: TmdbApiService,
    private val exceptionHandler: ExceptionHandler
) : MovieDataSource {


    override suspend fun getNowPlaying(page: Int): List<MovieModel> {
        return try {
            apiService.getNowPlayingMovies(page = page).results
        } catch (e: Exception) {
            throw Exception(exceptionHandler.getReadableMessage(e))
        }
    }

    override suspend fun getUpcoming(page: Int): List<MovieModel> {
        return try {
            apiService.getUpcomingMovies(page = page).results
        } catch (e: Exception) {
            throw Exception(exceptionHandler.getReadableMessage(e))
        }
    }

    override suspend fun getPopular(page: Int): List<MovieModel> {
        return try {
            apiService.getPopularMovies(page = page).results
        } catch (e: Exception) {
            throw Exception(exceptionHandler.getReadableMessage(e))
        }
    }
}