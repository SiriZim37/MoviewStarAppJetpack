package com.siri.database.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesLocalDataSource {

    companion object {
        val LAST_FETCH_KEY = longPreferencesKey("last_fetch_ms")
    }

    override suspend fun getLastFetchTimestamp(): Long = dataStore.data
        .map { preferences ->
            preferences[LAST_FETCH_KEY]
        }.first() ?: 0

    override suspend fun setLastFetchTimestamp(time: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_FETCH_KEY] = time
        }
    }
}