package com.example.scrollbooker.entity.calendar.connection.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.calendar.connection.data.remote.CalendarConnectionApiService
import com.example.scrollbooker.entity.calendar.connection.data.repository.CalendarConnectionRepositoryImpl
import com.example.scrollbooker.entity.calendar.connection.domain.repository.CalendarConnectionRepository
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.ConnectGoogleCalendarUseCase
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.DisconnectCalendarConnectionUseCase
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.GetCalendarConnectionUseCase
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
object CalendarConnectionModule {
    @Provides
    @Singleton
    fun provideCalendarConnectionApiService(okHttpClient: OkHttpClient): CalendarConnectionApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CalendarConnectionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCalendarConnectionRepository(
        apiService: CalendarConnectionApiService
    ): CalendarConnectionRepository {
        return CalendarConnectionRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetCalendarConnectionUseCase(
        repository: CalendarConnectionRepository
    ): GetCalendarConnectionUseCase {
        return GetCalendarConnectionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideConnectGoogleCalendarUseCase(
        repository: CalendarConnectionRepository
    ): ConnectGoogleCalendarUseCase {
        return ConnectGoogleCalendarUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDisconnectCalendarConnectionUseCase(
        repository: CalendarConnectionRepository
    ): DisconnectCalendarConnectionUseCase {
        return DisconnectCalendarConnectionUseCase(repository)
    }
}
