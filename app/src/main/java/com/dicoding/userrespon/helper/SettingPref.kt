package com.dicoding.userrespon.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPref private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME_KEY = booleanPreferencesKey("theme_settings")

    fun getThemeSettings(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSettings(isDarkModeActive: Boolean) {
        dataStore.edit { pref ->
            pref[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPref? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPref {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}