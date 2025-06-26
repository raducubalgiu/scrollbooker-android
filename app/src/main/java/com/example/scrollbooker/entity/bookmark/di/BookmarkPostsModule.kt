package com.example.scrollbooker.entity.bookmark.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.bookmark.data.remote.BookmarkPostsApiService
import com.example.scrollbooker.entity.bookmark.data.repository.BookmarkPostRepositoryImpl
import com.example.scrollbooker.entity.bookmark.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.entity.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
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
object BookmarkPostsModule {
    @Provides
    @Singleton
    fun provideBookmarkedPostsApiService(okHttpClient: OkHttpClient): BookmarkPostsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookmarkPostsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkedPostsRepository(apiService: BookmarkPostsApiService): BookmarkPostRepository {
        return BookmarkPostRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserBookmarkedPostsUseCase(
        repository: BookmarkPostRepository,
    ): GetUserBookmarkedPostsUseCase {
        return GetUserBookmarkedPostsUseCase(repository)
    }
}