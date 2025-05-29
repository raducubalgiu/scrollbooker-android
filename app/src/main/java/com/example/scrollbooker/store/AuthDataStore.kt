package com.example.scrollbooker.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context) {
    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = intPreferencesKey("user_id")
        val BUSINESS_ID = intPreferencesKey("business_id")
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        businessId: Int
    ) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
            it[USER_ID] = userId
            it[BUSINESS_ID] = businessId
        }
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }

    fun getUserId(): Flow<Int?> = context.dataStore.data.map { it[USER_ID] }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}