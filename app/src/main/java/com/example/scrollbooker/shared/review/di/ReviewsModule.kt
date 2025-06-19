package com.example.scrollbooker.shared.review.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.review.data.remote.ReviewsApiService
import com.example.scrollbooker.shared.review.data.repository.ReviewRepositoryImpl
import com.example.scrollbooker.shared.review.domain.repository.ReviewRepository
import com.example.scrollbooker.shared.review.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.shared.review.domain.useCase.GetReviewsUseCase
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