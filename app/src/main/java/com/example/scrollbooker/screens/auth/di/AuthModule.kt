package com.example.scrollbooker.screens.auth.di

import android.content.Context
import com.example.scrollbooker.core.network.authenticator.TokenAuthenticator
import com.example.scrollbooker.core.network.interceptor.AuthInterceptor
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.tokenProvider.TokenProviderImpl
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.screens.auth.data.remote.auth.AuthApiService
import com.example.scrollbooker.screens.auth.data.repository.AuthRepositoryImpl
import com.example.scrollbooker.screens.auth.domain.repository.AuthRepository
import com.example.scrollbooker.shared.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.shared.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.screens.auth.domain.usecase.LoginAndSaveSessionUseCase
import com.example.scrollbooker.store.AuthDataStore
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
    fun provideAuthRepository(
        apiService: AuthApiService,
        tokenProvider: TokenProvider,
        authDataStore: AuthDataStore
    ): AuthRepository {
        return AuthRepositoryImpl(
            apiService,
            tokenProvider,
            authDataStore
        )
    }

    @Provides
    @Singleton
    fun provideLoginAndSaveSessionUseCase(
        tokenProvider: TokenProvider,
        authDataStore: AuthDataStore,
        repository: AuthRepository,
        getUserInfoUseCase: GetUserInfoUseCase,
        getUserPermissionsUseCase: GetUserPermissionsUseCase,
    ): LoginAndSaveSessionUseCase {
        return LoginAndSaveSessionUseCase(
            tokenProvider = tokenProvider,
            authDataStore = authDataStore,
            repository = repository,
            getUserInfoUseCase = getUserInfoUseCase,
            getUserPermissionsUseCase = getUserPermissionsUseCase
        )
    }
}