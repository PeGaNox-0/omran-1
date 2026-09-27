package com.omran.quran.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.omran.quran.util.WeekDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val darkKey = booleanPreferencesKey("dark_theme")

    val darkTheme: Flow<Boolean?> = dataStore.data.map { it[darkKey] }

    fun workingDays(userId: String): Flow<Set<String>> = dataStore.data.map { prefs ->
        val userKey = stringSetPreferencesKey("working_days_${userId.ifBlank { "default" }}")
        val defaultKey = stringSetPreferencesKey("working_days")
        prefs[userKey] ?: prefs[defaultKey] ?: WeekDay.DEFAULT_WORKING_DAYS
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[darkKey] = enabled }
    }

    suspend fun setWorkingDays(userId: String, days: Set<String>) {
        val userKey = stringSetPreferencesKey("working_days_${userId.ifBlank { "default" }}")
        val defaultKey = stringSetPreferencesKey("working_days")
        dataStore.edit {
            it[userKey] = days
            it[defaultKey] = days
        }
    }
}
