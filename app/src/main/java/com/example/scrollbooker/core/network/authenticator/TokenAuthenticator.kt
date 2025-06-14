package com.example.scrollbooker.core.network.authenticator

import com.example.scrollbooker.feature.auth.data.remote.auth.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.auth.AuthDto
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val authApiService: AuthApiService
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            authDataStore.getRefreshToken().firstOrNull()
        } ?: return null

        return try {
            val refreshResponse = runBlocking {
                authApiService.refresh(AuthDto.RefreshRequestDto(refreshToken))
            }

            runBlocking {
                authDataStore.refreshTokens(
                    accessToken = refreshResponse.accessToken,
                    refreshToken = refreshResponse.refreshToken,
                )
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                .build()

        } catch (e: Exception) {
            runBlocking {
                authDataStore.clearUserSession()
            }

            null // Refresh failed -> Logout
        }
    }

}