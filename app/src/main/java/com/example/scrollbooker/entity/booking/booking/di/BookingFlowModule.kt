package com.example.scrollbooker.entity.booking.booking.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.booking.booking.data.remote.BookingFlowApiService
import com.example.scrollbooker.entity.booking.booking.data.repository.BookingFlowRepositoryImpl
import com.example.scrollbooker.entity.booking.booking.domain.repository.BookingFlowRepository
import com.example.scrollbooker.entity.booking.booking.domain.useCase.GetBookingFlowUseCase
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
object BookingFlowModule {
    @Provides
    @Singleton
    fun provideBookingFlowApiService(okHttpClient: OkHttpClient): BookingFlowApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookingFlowApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingFlowRepository(apiService: BookingFlowApiService): BookingFlowRepository {
        return BookingFlowRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetBookingFlowUseCase(
        repository: BookingFlowRepository,
    ): GetBookingFlowUseCase {
        return GetBookingFlowUseCase(repository)
    }
}