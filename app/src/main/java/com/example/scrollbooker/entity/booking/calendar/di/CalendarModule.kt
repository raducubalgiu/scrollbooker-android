package com.example.scrollbooker.entity.booking.calendar.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarApiService
import com.example.scrollbooker.entity.booking.calendar.data.repository.CalendarRepositoryImpl
import com.example.scrollbooker.entity.booking.calendar.domain.repository.CalendarRepository
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetUserCalendarEventsUseCase
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
object CalendarModule {
    @Provides
    @Singleton
    fun provideCalendarApiService(okHttpClient: OkHttpClient): CalendarApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CalendarApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCalendarRepository(apiService: CalendarApiService): CalendarRepository {
        return CalendarRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetCalendarAvailableDaysUseCase(
        repository: CalendarRepository,
    ): GetCalendarAvailableDaysUseCase {
        return GetCalendarAvailableDaysUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserAvailableTimeslotsUseCase(
        repository: CalendarRepository,
    ): GetUserAvailableTimeslotsUseCase {
        return GetUserAvailableTimeslotsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserCalendarEventsUseCase(
        repository: CalendarRepository,
    ): GetUserCalendarEventsUseCase {
        return GetUserCalendarEventsUseCase(repository)
    }
}