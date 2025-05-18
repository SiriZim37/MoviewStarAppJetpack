package com.siri.data.utils

import com.siri.database.datasource.MovieLocalDataSource
import com.siri.database.datasource.PreferencesLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/*
Diese Klasse prüft, ob der Cache noch gültig ist. Sie prüft zwei Dinge:
Ob die letzte Aktualisierung länger als 24 Stunden her ist เวลาที่โหลดล่าสุดเกิน 24 ชั่วโมงหรือยัง
Ob in der lokalen Datenbank überhaupt Daten vorhanden sind มีข้อมูลอยู่ใน local database หรือไม่
 */
class MovieCacheValidator @Inject constructor(
    private val preferencesDataSource: PreferencesLocalDataSource,
    private val localDataSource: MovieLocalDataSource,
) : CacheValidator {

    companion object {
        private const val CACHE_VALIDITY_TIME = 24 * 60 * 60 * 1000L // 24 hours in milliseconds
    }

    override suspend fun isValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastRefreshTime = preferencesDataSource.getLastFetchTimestamp()
        val isCacheOldEnough = (currentTime - lastRefreshTime) > CACHE_VALIDITY_TIME
        val isLocalStorageEmpty = localDataSource.getMovies().firstOrNull()?.isEmpty() ?: true

        return !isCacheOldEnough && !isLocalStorageEmpty
    }
}