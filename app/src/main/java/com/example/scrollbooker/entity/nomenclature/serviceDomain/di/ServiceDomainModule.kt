package com.example.scrollbooker.entity.nomenclature.serviceDomain.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainApiService
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.repository.ServiceDomainRepositoryImpl
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetAllServiceDomainsByBusinessDomainUseCase
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
object ServiceDomainModule {
    @Provides
    @Singleton
    fun provideServiceDomainApiService(okHttpClient: OkHttpClient): ServiceDomainApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceDomainApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideServiceDomainRepository(apiService: ServiceDomainApiService): ServiceDomainRepository {
        return ServiceDomainRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAllServiceDomainsByBusinessDomainUseCase(
        repository: ServiceDomainRepository,
    ): GetAllServiceDomainsByBusinessDomainUseCase {
        return GetAllServiceDomainsByBusinessDomainUseCase(repository)
    }
}