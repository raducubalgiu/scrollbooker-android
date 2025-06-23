package com.example.scrollbooker.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scrollbooker.shared.user.userInfo.domain.model.RegistrationStepEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context) {
    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        val PERMISSIONS = stringSetPreferencesKey("permissions")
        val USER_ID = intPreferencesKey("userId")
        val BUSINESS_ID = intPreferencesKey("businessId")
        val BUSINESS_TYPE_ID = intPreferencesKey("businessTypeId")
        val IS_VALIDATED = booleanPreferencesKey("is_validated")
        val REGISTRATION_STEP = stringPreferencesKey("registrationStep")
    }

    // Store Entire Session
    suspend fun storeUserSession(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        businessId: Int?,
        businessTypeId: Int?,
        permissions: List<String>,
        isValidated: Boolean,
        registrationStep: RegistrationStepEnum?
    ) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[USER_ID] = userId
            prefs[BUSINESS_ID] = businessId ?: prefs.remove(BUSINESS_ID)
            prefs[BUSINESS_TYPE_ID] = businessTypeId ?: prefs.remove(BUSINESS_TYPE_ID)
            prefs[PERMISSIONS] = permissions.toSet()
            prefs[IS_VALIDATED] = isValidated
            registrationStep?.let {
                prefs[REGISTRATION_STEP] = it.key
            } ?: prefs.remove(REGISTRATION_STEP)
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

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }
    fun getUserId(): Flow<Int?> = context.dataStore.data.map { it[USER_ID] }
    fun getBusinessId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_ID] }
    fun getBusinessTypeId(): Flow<Int?> = context.dataStore.data.map { it[BUSINESS_TYPE_ID] }
    fun getUserPermissions(): Flow<List<String>> = context.dataStore.data
        .map { prefs -> prefs[PERMISSIONS]?.toList() ?: emptyList() }
    fun getIsValidated(): Flow<Boolean?> = context.dataStore.data.map { it[IS_VALIDATED] }
    fun getRegistrationStep(): Flow<RegistrationStepEnum?> = context.dataStore.data
        .map { prefs ->
            val stepRaw = prefs[REGISTRATION_STEP]
            RegistrationStepEnum.fromKeyOrNull(stepRaw)
        }

    suspend fun clearUserSession() {
        context.dataStore.edit { it.clear() }
    }
}