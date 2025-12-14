package com.example.scrollbooker.entity.booking.appointment.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentsApiService
import com.example.scrollbooker.entity.booking.appointment.data.repository.AppointmentRepositoryImpl
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateBlockAppointmentsUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateLastMinuteAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateOwnClientAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateScrollBookerAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.DeleteAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsUseCase
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
object AppointmentsModule {
    @Provides
    @Singleton
    fun provideAppointmentsApiService(okHttpClient: OkHttpClient): AppointmentsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppointmentsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppointmentsRepository(apiService: AppointmentsApiService): AppointmentRepository {
        return AppointmentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAppointmentsUseCase(
        repository: AppointmentRepository,
    ): GetUserAppointmentsUseCase {
        return GetUserAppointmentsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAppointmentsNumberUseCase(
        repository: AppointmentRepository,
    ): GetUserAppointmentsNumberUseCase {
        return GetUserAppointmentsNumberUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateScrollBookerAppointmentsUseCase(
        repository: AppointmentRepository,
    ): CreateScrollBookerAppointmentUseCase {
        return CreateScrollBookerAppointmentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateOwnClientAppointmentsUseCase(
        repository: AppointmentRepository,
    ): CreateOwnClientAppointmentUseCase {
        return CreateOwnClientAppointmentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateLastMinuteAppointmentsUseCase(
        repository: AppointmentRepository,
    ): CreateLastMinuteAppointmentUseCase {
        return CreateLastMinuteAppointmentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateBlockAppointmentsUseCase(
        repository: AppointmentRepository,
    ): CreateBlockAppointmentsUseCase {
        return CreateBlockAppointmentsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteAppointmentUseCase(
        repository: AppointmentRepository,
    ): DeleteAppointmentUseCase {
        return DeleteAppointmentUseCase(repository)
    }
}