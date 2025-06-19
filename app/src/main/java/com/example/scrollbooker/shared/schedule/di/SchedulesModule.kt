package com.example.scrollbooker.shared.schedule.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.schedule.data.remote.SchedulesApiService
import com.example.scrollbooker.shared.schedule.data.repository.ScheduleRepositoryImpl
import com.example.scrollbooker.shared.schedule.domain.repository.ScheduleRepository
import com.example.scrollbooker.shared.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.shared.schedule.domain.useCase.UpdateSchedulesUseCase
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
object SchedulesModule {
    @Provides
    @Singleton
    fun provideSchedulesApiService(okHttpClient: OkHttpClient): SchedulesApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SchedulesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(apiService: SchedulesApiService): ScheduleRepository {
        return ScheduleRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetSchedulesByUserIdUseCase(
        repository: ScheduleRepository
    ): GetSchedulesByUserIdUseCase {
        return GetSchedulesByUserIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateSchedulesUseCase(
        repository: ScheduleRepository
    ): UpdateSchedulesUseCase {
        return UpdateSchedulesUseCase(repository)
    }
}