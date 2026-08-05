package com.example.scrollbooker.entity.onboarding.di

import android.content.Context
import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.onboarding.data.remote.OnboardingApiService
import com.example.scrollbooker.entity.onboarding.data.repository.OnboardingRepositoryImpl
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessCurrenciesUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessGalleryUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessHasEmployeesUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessSchedulesUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessServicesUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectClientBirthdateUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectClientGenderUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectUserLocationPermissionUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectUserUsernameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnboardingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        apiService: OnboardingApiService,
        @ApplicationContext context: Context
    ): OnboardingRepository {
        return OnboardingRepositoryImpl(apiService, context)
    }

    @Provides
    @Singleton
    fun provideCollectUserUsernameUseCase(
        repository: OnboardingRepository,
    ): CollectUserUsernameUseCase {
        return CollectUserUsernameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectClientBirthdateUseCase(
        repository: OnboardingRepository,
    ): CollectClientBirthdateUseCase {
        return CollectClientBirthdateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectClientGenderUseCase(
        repository: OnboardingRepository,
    ): CollectClientGenderUseCase {
        return CollectClientGenderUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectUserLocationPermission(
        repository: OnboardingRepository,
    ): CollectUserLocationPermissionUseCase {
        return CollectUserLocationPermissionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessUseCase {
        return CollectBusinessUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessServicesUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessServicesUseCase {
        return CollectBusinessServicesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessSchedulesUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessSchedulesUseCase {
        return CollectBusinessSchedulesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessHasEmployeesUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessHasEmployeesUseCase {
        return CollectBusinessHasEmployeesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessCurrenciesUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessCurrenciesUseCase {
        return CollectBusinessCurrenciesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCollectBusinessGalleryUseCase(
        repository: OnboardingRepository,
    ): CollectBusinessGalleryUseCase {
        return CollectBusinessGalleryUseCase(repository)
    }
}