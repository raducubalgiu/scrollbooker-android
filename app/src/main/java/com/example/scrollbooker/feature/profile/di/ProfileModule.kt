package com.example.scrollbooker.feature.profile.di

import com.example.scrollbooker.feature.profile.data.remote.UserApiService
import com.example.scrollbooker.feature.profile.data.repository.UserRepositoryImpl
import com.example.scrollbooker.feature.profile.domain.repository.UserRepository
import com.example.scrollbooker.feature.profile.domain.usecase.GetUserInfoUseCase
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
object ProfileModule {

    @Provides
    @Singleton
    fun provideUserApiService(okHttpClient: OkHttpClient): UserApiService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/v1/aut/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService
    ): UserRepository = UserRepositoryImpl(userApiService)

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: UserRepository): GetUserInfoUseCase =
        GetUserInfoUseCase(repository)
}