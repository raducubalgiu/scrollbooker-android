package com.example.scrollbooker.feature.auth.di

import com.example.scrollbooker.core.util.Constants.BASE_URL
import com.example.scrollbooker.feature.auth.data.remote.UserInfoApiService
import com.example.scrollbooker.feature.auth.data.repository.UserInfoRepositoryImpl
import com.example.scrollbooker.feature.auth.domain.repository.UserInfoRepository
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserInfoModule {
    @Provides
    @Singleton
    fun provideUserInfoApiService(okHttpClient: OkHttpClient): UserInfoApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserInfoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserInfoRepository(
        apiService: UserInfoApiService
    ): UserInfoRepository = UserInfoRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        repository: UserInfoRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository)
    }
}