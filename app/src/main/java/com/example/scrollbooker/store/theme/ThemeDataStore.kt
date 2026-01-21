package com.example.scrollbooker.store.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scrollbooker.store.util.ThemePreferenceEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemeDataStore(private val context: Context) {
    companion object {
        private val THEME_PREF_KEY = stringPreferencesKey("theme_pref")
    }

    val themePreferenceFlow: Flow<ThemePreferenceEnum> = context.themeDataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
        .map { preferences ->
            val pref = preferences[THEME_PREF_KEY] ?: ThemePreferenceEnum.SYSTEM.name
            ThemePreferenceEnum.valueOf(pref)
        }
        .distinctUntilChanged()

    suspend fun saveThemePreference(themePreferenceEnum: ThemePreferenceEnum) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_PREF_KEY] = themePreferenceEnum.name
        }
    }
}