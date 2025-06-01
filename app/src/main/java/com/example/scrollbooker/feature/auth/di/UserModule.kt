package com.example.scrollbooker.feature.auth.di

import android.content.Context
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.auth.data.repository.UserRepositoryImpl
import com.example.scrollbooker.feature.auth.domain.repository.UserRepository
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserInfoUseCase
import com.example.scrollbooker.feature.auth.data.remote.UserApiService
import com.example.scrollbooker.store.AuthDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApiService(okHttpClient: OkHttpClient): UserApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userApiService: UserApiService): GetUserInfoUseCase =
        GetUserInfoUseCase(userApiService)

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService
    ): UserRepository = UserRepositoryImpl(userApiService)
}