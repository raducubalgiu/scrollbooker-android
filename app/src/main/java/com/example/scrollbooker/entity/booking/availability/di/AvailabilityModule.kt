package com.example.scrollbooker.entity.booking.availability.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.availability.data.remote.AvailabilityApiService
import com.example.scrollbooker.entity.booking.availability.data.repository.AvailabilityRepositoryImpl
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserCalendarEventsUseCase
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
object AvailabilityModule {
    @Provides
    @Singleton
    fun provideAvailabilityApiService(okHttpClient: OkHttpClient): AvailabilityApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AvailabilityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAvailabilityRepository(apiService: AvailabilityApiService): AvailabilityRepository {
        return AvailabilityRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetCalendarAvailableDaysUseCase(
        repository: AvailabilityRepository,
    ): GetCalendarAvailableDaysUseCase {
        return GetCalendarAvailableDaysUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserAvailableTimeslotsUseCase(
        repository: AvailabilityRepository,
    ): GetUserAvailableTimeslotsUseCase {
        return GetUserAvailableTimeslotsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserCalendarEventsUseCase(
        repository: AvailabilityRepository,
    ): GetUserCalendarEventsUseCase {
        return GetUserCalendarEventsUseCase(repository)
    }
}