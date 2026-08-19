package com.example.scrollbooker.entity.dashboard.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.dashboard.data.remote.DashboardApiService
import com.example.scrollbooker.entity.dashboard.data.repository.DashboardRepositoryImpl
import com.example.scrollbooker.entity.dashboard.domain.repository.DashboardRepository
import com.example.scrollbooker.entity.dashboard.domain.useCase.GetDashboardBookingUseCase
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
    fun provideDashboardApiService(okHttpClient: OkHttpClient): DashboardApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashboardApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardRepository(apiService: DashboardApiService): DashboardRepository {
        return DashboardRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetDashboardBookingUseCase(
        repository: DashboardRepository,
    ): GetDashboardBookingUseCase {
        return GetDashboardBookingUseCase(repository)
    }
}