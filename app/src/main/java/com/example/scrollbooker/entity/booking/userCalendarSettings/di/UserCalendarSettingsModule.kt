package com.example.scrollbooker.entity.booking.userCalendarSettings.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote.UserCalendarSettingsApiService
import com.example.scrollbooker.entity.booking.userCalendarSettings.data.repository.UserCalendarSettingsRepositoryImpl
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.repository.UserCalendarSettingsRepository
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.useCase.GetUserCalendarSettingsUseCase
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.useCase.UpdateAppointmentGapUseCase
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.useCase.UpdateSlotDurationUseCase
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
object UserCalendarSettingsModule {
    @Provides
    @Singleton
    fun provideUserCalendarSettingsApiService(okHttpClient: OkHttpClient): UserCalendarSettingsApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserCalendarSettingsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserCalendarSettingsRepository(
        apiService: UserCalendarSettingsApiService
    ): UserCalendarSettingsRepository {
        return UserCalendarSettingsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserCalendarSettingsUseCase(
        repository: UserCalendarSettingsRepository
    ): GetUserCalendarSettingsUseCase {
        return GetUserCalendarSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateSlotDurationUseCase(
        repository: UserCalendarSettingsRepository
    ): UpdateSlotDurationUseCase {
        return UpdateSlotDurationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateAppointmentGapUseCase(
        repository: UserCalendarSettingsRepository
    ): UpdateAppointmentGapUseCase {
        return UpdateAppointmentGapUseCase(repository)
    }
}
