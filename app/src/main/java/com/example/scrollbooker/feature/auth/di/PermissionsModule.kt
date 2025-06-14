package com.example.scrollbooker.feature.auth.di

import com.example.scrollbooker.core.util.Constants.BASE_URL
import com.example.scrollbooker.feature.auth.data.remote.userPermissions.PermissionsApiService
import com.example.scrollbooker.feature.auth.data.repository.PermissionRepositoryImpl
import com.example.scrollbooker.feature.auth.domain.repository.PermissionRepository
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserPermissionsUseCase
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
object PermissionsModule {
    @Provides
    @Singleton
    fun providePermissionsApiService(okHttpClient: OkHttpClient): PermissionsApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PermissionsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserPermissionsRepository(
        apiService: PermissionsApiService
    ): PermissionRepository = PermissionRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideGetUserPermissionsUseCase(
        repository: PermissionRepository
    ): GetUserPermissionsUseCase {
        return GetUserPermissionsUseCase(repository)
    }
}