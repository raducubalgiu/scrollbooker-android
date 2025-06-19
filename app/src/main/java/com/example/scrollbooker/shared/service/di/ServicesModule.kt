package com.example.scrollbooker.shared.service.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.service.data.remote.ServicesApiService
import com.example.scrollbooker.shared.service.data.repository.ServiceRepositoryImpl
import com.example.scrollbooker.shared.service.domain.repository.ServiceRepository
import com.example.scrollbooker.shared.service.domain.useCase.AttachManyServicesUseCase
import com.example.scrollbooker.shared.service.domain.useCase.DetachServiceUseCase
import com.example.scrollbooker.shared.service.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.shared.service.domain.useCase.GetServicesByUserIdUseCase
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
        repository: ServiceRepository
    ): GetServicesByUserIdUseCase {
        return GetServicesByUserIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetServicesByBusinessTypeUseCase(
        repository: ServiceRepository,
    ): GetServicesByBusinessTypeUseCase {
        return GetServicesByBusinessTypeUseCase(repository)
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