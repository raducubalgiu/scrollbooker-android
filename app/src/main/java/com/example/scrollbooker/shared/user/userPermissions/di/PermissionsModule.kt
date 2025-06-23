package com.example.scrollbooker.shared.user.userPermissions.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.user.userPermissions.domain.repository.PermissionRepository
import com.example.scrollbooker.shared.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.shared.user.userPermissions.data.remote.PermissionsApiService
import com.example.scrollbooker.shared.user.userPermissions.data.repository.PermissionRepositoryImpl
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
            .baseUrl(Constants.BASE_URL)
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