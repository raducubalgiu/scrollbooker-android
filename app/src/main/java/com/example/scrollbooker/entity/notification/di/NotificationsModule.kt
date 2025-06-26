package com.example.scrollbooker.entity.notification.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.notification.data.remote.NotificationsApiService
import com.example.scrollbooker.entity.notification.data.repository.NotificationRepositoryImpl
import com.example.scrollbooker.entity.notification.domain.repository.NotificationRepository
import com.example.scrollbooker.entity.notification.domain.useCase.GetNotificationsUseCase
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
object NotificationsModule {
    @Provides
    @Singleton
    fun provideNotificationsApiService(okHttpClient: OkHttpClient): NotificationsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationsRepository(apiService: NotificationsApiService): NotificationRepository {
        return NotificationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetNotificationsUseCase(
        repository: NotificationRepository,
    ): GetNotificationsUseCase {
        return GetNotificationsUseCase(repository)
    }
}