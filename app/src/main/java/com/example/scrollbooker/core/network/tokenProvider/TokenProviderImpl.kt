package com.example.scrollbooker.core.network.tokenProvider

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProviderImpl @Inject constructor(): TokenProvider {

    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var refreshToken: String? = null

    override fun getAccessToken(): String? = accessToken

    override fun getRefreshToken(): String? = refreshToken

    override fun updateTokens(accessToken: String, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun clearTokens() {
        accessToken = null
        refreshToken = null
    }

}