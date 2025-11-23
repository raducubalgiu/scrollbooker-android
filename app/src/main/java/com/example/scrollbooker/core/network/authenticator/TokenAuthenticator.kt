package com.example.scrollbooker.core.network.authenticator
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.entity.auth.data.remote.AuthApiService
import com.example.scrollbooker.entity.auth.data.remote.AuthDto
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val authApiService: AuthApiService,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if(response.isAlreadyRetried()) return null

        val refreshToken = tokenProvider.getRefreshToken()
            ?: runBlocking { authDataStore.getRefreshToken().firstOrNull() }
            ?: return null

        return try {
            val refreshResponse = runBlocking {
                authApiService.refresh(
                    AuthDto.RefreshRequestDto(refreshToken)
                )
            }

            tokenProvider.updateTokens(
                accessToken = refreshResponse.accessToken,
                refreshToken = refreshResponse.refreshToken,
            )

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
            Timber.tag("Token Provider").e("ERROR: on Refreshing Token: $e")
            runBlocking { authDataStore.clearUserSession() }
            null
        }
    }
}

private fun Response.isAlreadyRetried(): Boolean {
    var current: Response? = this
    var count = 0
    while (current != null) {
        count++
        current = current.priorResponse
    }
    return count >= 2
}