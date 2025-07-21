package com.example.scrollbooker.entity.onboarding.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.onboarding.data.remote.OnboardingApiService
import com.example.scrollbooker.entity.onboarding.data.repository.OnboardingRepositoryImpl
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectUserUsernameUseCase
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
object BusinessModule {
    @Provides
    @Singleton
    fun provideOnboardingApiService(okHttpClient: OkHttpClient): OnboardingApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnboardingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(apiService: OnboardingApiService): OnboardingRepository {
        return OnboardingRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCollectUserUsernameUseCase(
        repository: OnboardingRepository,
    ): CollectUserUsernameUseCase {
        return CollectUserUsernameUseCase(repository)
    }
}