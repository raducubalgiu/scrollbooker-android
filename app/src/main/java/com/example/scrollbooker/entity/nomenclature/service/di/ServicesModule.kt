package com.example.scrollbooker.entity.nomenclature.service.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServicesApiService
import com.example.scrollbooker.entity.nomenclature.service.data.repository.ServiceRepositoryImpl
import com.example.scrollbooker.entity.nomenclature.service.domain.repository.ServiceRepository
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByServiceDomainUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByUserIdUseCase
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
    fun provideGetServicesByServiceDomainIdUseCase(
        repository: ServiceRepository,
    ): GetServicesByServiceDomainUseCase {
        return GetServicesByServiceDomainUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetServicesByUserIdUseCase(
        repository: ServiceRepository,
    ): GetServicesByUserIdUseCase {
        return GetServicesByUserIdUseCase(repository)
    }
}