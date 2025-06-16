package com.example.scrollbooker.feature.reviews.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.reviews.data.remote.ReviewsApiService
import com.example.scrollbooker.feature.reviews.data.repository.ReviewRepositoryImpl
import com.example.scrollbooker.feature.reviews.domain.repository.ReviewRepository
import com.example.scrollbooker.feature.reviews.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.feature.reviews.domain.useCase.GetReviewsUseCase
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
object ReviewsModule {
    @Provides
    @Singleton
    fun provideReviewsApiService(okHttpClient: OkHttpClient): ReviewsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewsRepository(apiService: ReviewsApiService): ReviewRepository {
        return ReviewRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetReviewsUseCase(
        repository: ReviewRepository,
    ): GetReviewsUseCase {
        return GetReviewsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetReviewsSummaryUseCase(
        repository: ReviewRepository,
    ): GetReviewsSummaryUseCase {
        return GetReviewsSummaryUseCase(repository)
    }
}