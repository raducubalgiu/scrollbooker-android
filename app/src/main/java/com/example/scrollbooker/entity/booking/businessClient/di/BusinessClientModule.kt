package com.example.scrollbooker.entity.booking.businessClient.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.booking.businessClient.data.remote.BusinessClientsApiService
import com.example.scrollbooker.entity.booking.businessClient.data.repository.BusinessClientRepositoryImpl
import com.example.scrollbooker.entity.booking.businessClient.domain.repository.BusinessClientRepository
import com.example.scrollbooker.entity.booking.businessClient.domain.useCase.CreateBusinessClientUseCase
import com.example.scrollbooker.entity.booking.businessClient.domain.useCase.SearchBusinessClientsUseCase
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
object BusinessClientModule {
    @Provides
    @Singleton
    fun provideBusinessClientsApiService(okHttpClient: OkHttpClient): BusinessClientsApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BusinessClientsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBusinessClientRepository(apiService: BusinessClientsApiService): BusinessClientRepository {
        return BusinessClientRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchBusinessClientsUseCase(
        repository: BusinessClientRepository,
    ): SearchBusinessClientsUseCase {
        return SearchBusinessClientsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateBusinessClientUseCase(
        repository: BusinessClientRepository,
    ): CreateBusinessClientUseCase {
        return CreateBusinessClientUseCase(repository)
    }
}
