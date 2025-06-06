package com.example.scrollbooker.core.network.util

import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val authDataStore: AuthDataStore
) {
    @Volatile
    private var cachedAccessToken: String? = null

    @Volatile
    private var cachedRefreshToken: String? = null

    suspend fun preloadTokens() {
        authDataStore.getAccessToken().firstOrNull()?.let {
            cachedAccessToken = it
        }
        authDataStore.getRefreshToken().firstOrNull()?.let {
            cachedRefreshToken = it
        }
    }

    fun getAccessToken(): String? = cachedAccessToken
    fun getRefreshToken(): String? = cachedRefreshToken

    fun updateTokens(access: String?, refresh: String?) {
        cachedAccessToken = access
        cachedRefreshToken = refresh
    }

    fun clearTokens() {
        cachedAccessToken = null
        cachedRefreshToken = null
    }
}