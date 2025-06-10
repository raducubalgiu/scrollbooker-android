package com.example.scrollbooker.feature.consents.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.consents.data.remote.ConsentsApiService
import com.example.scrollbooker.feature.consents.data.repository.ConsentRepositoryImpl
import com.example.scrollbooker.feature.consents.domain.repository.ConsentRepository
import com.example.scrollbooker.feature.consents.domain.useCase.GetConsentsByNameUseCase
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
object ConsentsModule {
    @Provides
    @Singleton
    fun provideConsentsApiService(okHttpClient: OkHttpClient): ConsentsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsentsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideConsentsRepository(apiService: ConsentsApiService): ConsentRepository {
        return ConsentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetConsentsByNameUseCase(
        repository: ConsentRepository,
    ): GetConsentsByNameUseCase {
        return GetConsentsByNameUseCase(repository)
    }
}