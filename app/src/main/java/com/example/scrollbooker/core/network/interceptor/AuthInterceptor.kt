package com.example.scrollbooker.core.network.interceptor

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.store.AuthDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val tokenProvider: TokenProvider
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getAccessToken() ?: authDataStore.getAccessToken()

        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())

//        val token = runBlocking {
//            authDataStore.getAccessToken().firstOrNull()
//        }
//
//        val original = chain.request()
//        val requestBuilder = original.newBuilder()
//
//        token?.let {
//            requestBuilder.addHeader("Authorization", "Bearer $it")
//        }
//
//        val request = requestBuilder.build()
//        return chain.proceed(request)
    }

}