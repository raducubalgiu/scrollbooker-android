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
        val BUSINESS_OWNER_ID = intPreferencesKey("businessOwnerId")
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
        businessOwnerId: Int?,
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
            prefs[HAS_EMPLOYEES] = hasEmployees
            prefs[PERMISSIONS] = permissions.toSet()

            if (businessId != null) prefs[BUSINESS_ID] = businessId else prefs.remove(BUSINESS_ID)
            if (businessOwnerId != null) prefs[BUSINESS_OWNER_ID] = businessOwnerId else prefs.remove(BUSINESS_OWNER_ID)
            if (businessTypeId != null) prefs[BUSINESS_TYPE_ID] = businessTypeId else prefs.remove(BUSINESS_TYPE_ID)
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

    suspend fun setBusinessId(businessId: Int?) {
        context.dataStore.edit { prefs ->
            if (businessId != null) prefs[BUSINESS_ID] = businessId else prefs.remove(BUSINESS_ID)
        }
    }

    suspend fun setBusinessOwnerId(businessOwnerId: Int?) {
        context.dataStore.edit { prefs ->
            if (businessOwnerId != null) prefs[BUSINESS_OWNER_ID] = businessOwnerId else prefs.remove(BUSINESS_OWNER_ID)
        }
    }

    suspend fun setBusinessTypeId(businessTypeId: Int?) {
        context.dataStore.edit { prefs ->
            if (businessTypeId != null) prefs[BUSINESS_TYPE_ID] = businessTypeId else prefs.remove(BUSINESS_TYPE_ID)
        }
    }

    suspend fun setHasEmployees(hasEmployees: Boolean) {
        context.dataStore.edit { prefs -> prefs[HAS_EMPLOYEES] = hasEmployees }
    }

    suspend fun setBusinessTypeId(businessTypeId: Int) {
        context.dataStore.edit { prefs -> prefs[BUSINESS_TYPE_ID] = businessTypeId }
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
    fun getUserUsername(): Flow<String?> = context.dataStore.data.map { it[USERNAME] }
    fun getBusinessId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_ID] }
    fun getBusinessOwnerId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_OWNER_ID] }
    fun getBusinessTypeId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_TYPE_ID] }
    fun getHasEmployees(): Flow<Boolean?> = context.dataStore.data.map { it[HAS_EMPLOYEES] }
    fun getUserPermissions(): Flow<List<String>> = context.dataStore.data
        .map { prefs -> prefs[PERMISSIONS]?.toList() ?: emptyList() }

    suspend fun clearUserSession() {
        context.dataStore.edit { it.clear() }
    }
}