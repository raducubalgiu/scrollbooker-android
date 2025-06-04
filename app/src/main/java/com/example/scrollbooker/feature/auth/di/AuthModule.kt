package com.example.scrollbooker.feature.auth.di

import android.content.Context
import com.example.scrollbooker.core.network.authenticator.TokenAuthenticator
import com.example.scrollbooker.core.network.interceptor.AuthInterceptor
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.tokenProvider.TokenProviderImpl
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import com.example.scrollbooker.feature.auth.data.repository.AuthRepositoryImpl
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import com.example.scrollbooker.feature.auth.domain.usecase.GetLoginUseCase
import com.example.scrollbooker.feature.user.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserPermissionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideTokenProvider(): TokenProvider = TokenProviderImpl()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenProvider: TokenProvider,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(tokenProvider))
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): AuthDataStore =
        AuthDataStore(context)

    @Provides
    @Singleton
    fun provideGetLoginInfoUseCase(authApi: AuthApiService): GetLoginUseCase {
        return GetLoginUseCase(authApi)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        tokenProvider: TokenProvider,
        authDataStore: AuthDataStore,
        getLoginUseCase: GetLoginUseCase,
        getUserInfoUseCase: GetUserInfoUseCase,
        getUserPermissionsUseCase: GetUserPermissionsUseCase
    ): AuthRepository =
        AuthRepositoryImpl(
            authApi = authApiService,
            tokenProvider = tokenProvider,
            authDataStore = authDataStore,
            getLoginInfoUseCase = getLoginUseCase,
            getUserInfoUseCase = getUserInfoUseCase,
            getUserPermissionsUseCase = getUserPermissionsUseCase
        )
}