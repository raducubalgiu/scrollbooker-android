package com.example.scrollbooker.core.network.authenticator

import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.AuthDto
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
                authDataStore.saveTokens(
                    accessToken = refreshResponse.accessToken,
                    refreshToken = refreshResponse.refreshToken,
                    userId = refreshResponse.userId,
                    businessId = refreshResponse.businessId
                )
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                .build()

        } catch (e: Exception) {
            runBlocking {
                authDataStore.clearTokens()
            }

            null // Refresh failed -> Logout
        }
    }

}