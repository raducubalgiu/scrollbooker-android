package com.example.scrollbooker.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

data class TokensPrefs(
    val accessToken: String?,
    val refreshToken: String?,
)

class AuthDataStore(private val context: Context) {
    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        val PERMISSIONS = stringSetPreferencesKey("permissions")
        val USER_ID = intPreferencesKey("userId")
        val USERNAME = stringPreferencesKey("username")
        val FULLNAME = stringPreferencesKey("fullName")
        val BUSINESS_ID = intPreferencesKey("businessId")
        val BUSINESS_TYPE_ID = intPreferencesKey("businessTypeId")
        val HAS_EMPLOYEES = booleanPreferencesKey("hasEmployees")
    }

    // Store Entire Session
    suspend fun storeUserSession(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        username: String,
        fullName: String,
        businessId: Int?,
        businessTypeId: Int?,
        hasEmployees: Boolean,
        permissions: List<String>,
    ) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[USER_ID] = userId
            prefs[USERNAME] = username
            prefs[FULLNAME] = fullName
            prefs[BUSINESS_ID] = businessId ?: prefs.remove(BUSINESS_ID)
            prefs[BUSINESS_TYPE_ID] = businessTypeId ?: prefs.remove(BUSINESS_TYPE_ID)
            prefs[HAS_EMPLOYEES] = hasEmployees
            prefs[PERMISSIONS] = permissions.toSet()
        }
    }

    suspend fun refreshTokens(
        accessToken: String,
        refreshToken: String
    ) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun setBusinessId(businessId: Int) {
        context.dataStore.edit { prefs -> prefs[BUSINESS_ID] = businessId }
    }

    suspend fun setBusinessTypeId(businessTypeId: Int) {
        context.dataStore.edit { prefs -> prefs[BUSINESS_TYPE_ID] = businessTypeId }
    }

    suspend fun setHasEmployees(hasEmployees: Boolean) {
        context.dataStore.edit { prefs -> prefs[HAS_EMPLOYEES] = hasEmployees }
    }

    val tokensPrefs: Flow<TokensPrefs> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                TokensPrefs(
                    accessToken = prefs[ACCESS_TOKEN],
                    refreshToken = prefs[REFRESH_TOKEN]
                )
            }

    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }
    fun getUserId(): Flow<Int?> = context.dataStore.data.map { it[USER_ID] }
    fun getBusinessId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_ID] }
    fun getBusinessTypeId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_TYPE_ID] }
    fun getHasEmployees(): Flow<Boolean> = context.dataStore.data.map { it[HAS_EMPLOYEES] == true }
    fun getUserPermissions(): Flow<List<String>> = context.dataStore.data
        .map { prefs -> prefs[PERMISSIONS]?.toList() ?: emptyList() }

    suspend fun clearUserSession() {
        context.dataStore.edit { it.clear() }
    }
}