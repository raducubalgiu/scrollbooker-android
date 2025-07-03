package com.example.scrollbooker.entity.service.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.service.data.remote.ServicesApiService
import com.example.scrollbooker.entity.service.data.repository.ServiceRepositoryImpl
import com.example.scrollbooker.entity.service.domain.repository.ServiceRepository
import com.example.scrollbooker.entity.service.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.entity.service.domain.useCase.GetServicesByBusinessIdUseCase
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
    fun provideGetServicesByBusinessIdUseCase(
        repository: ServiceRepository
    ): GetServicesByBusinessIdUseCase {
        return GetServicesByBusinessIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetServicesByBusinessTypeIdUseCase(
        repository: ServiceRepository,
    ): GetServicesByBusinessTypeUseCase {
        return GetServicesByBusinessTypeUseCase(repository)
    }
}