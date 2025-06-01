package com.example.scrollbooker.core.network.tokenProvider

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun updateTokens(accessToken: String, refreshToken: String?)
    fun clearTokens()
}