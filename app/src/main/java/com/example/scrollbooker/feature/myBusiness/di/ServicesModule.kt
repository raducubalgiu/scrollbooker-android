package com.example.scrollbooker.feature.myBusiness.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.myBusiness.data.remote.service.ServicesApiService
import com.example.scrollbooker.feature.myBusiness.data.repository.ServiceRepositoryImpl
import com.example.scrollbooker.feature.myBusiness.domain.repository.ServiceRepository
import com.example.scrollbooker.feature.myBusiness.domain.useCase.services.GetServicesUseCase
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
}