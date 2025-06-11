package com.example.scrollbooker.feature.services.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.services.data.remote.ServicesApiService
import com.example.scrollbooker.feature.services.data.repository.ServiceRepositoryImpl
import com.example.scrollbooker.feature.services.domain.repository.ServiceRepository
import com.example.scrollbooker.feature.services.domain.useCase.AttachManyServicesUseCase
import com.example.scrollbooker.feature.services.domain.useCase.DetachServiceUseCase
import com.example.scrollbooker.feature.services.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.feature.services.domain.useCase.GetServicesUseCase
import com.example.scrollbooker.store.AuthDataStore
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
object ServicesModule {

    @Provides
    @Singleton
    fun provideServicesApiService(okHttpClient: OkHttpClient): ServicesApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideServiceRepository(apiService: ServicesApiService): ServiceRepository {
        return ServiceRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetServicesUseCase(
        repository: ServiceRepository,
        authDataStore: AuthDataStore
    ): GetServicesUseCase {
        return GetServicesUseCase(repository, authDataStore)
    }

    @Provides
    @Singleton
    fun provideGetServicesByBusinessTypeUseCase(
        repository: ServiceRepository,
        authDataStore: AuthDataStore
    ): GetServicesByBusinessTypeUseCase {
        return GetServicesByBusinessTypeUseCase(repository, authDataStore)
    }

    @Provides
    @Singleton
    fun provideAttachManyServicesUseCase(
        repository: ServiceRepository,
        authDataStore: AuthDataStore
    ): AttachManyServicesUseCase {
        return AttachManyServicesUseCase(authDataStore, repository)
    }

    @Provides
    @Singleton
    fun provideDetachServiceUseCase(
        authDataStore: AuthDataStore,
        repository: ServiceRepository
    ): DetachServiceUseCase {
        return DetachServiceUseCase(authDataStore, repository)
    }
}