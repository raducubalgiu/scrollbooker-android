package com.example.scrollbooker.shared.comment.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.comment.data.remote.CommentsApiService
import com.example.scrollbooker.shared.comment.data.repository.CommentRepositoryImpl
import com.example.scrollbooker.shared.comment.domain.repository.CommentRepository
import com.example.scrollbooker.shared.comment.domain.useCase.GetPaginatedPostCommentsUseCase
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
object CommentsModule {
    @Provides
    @Singleton
    fun provideGetPostCommentsApiService(okHttpClient: OkHttpClient): CommentsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentsApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePostCommentsRepository(apiService: CommentsApiService): CommentRepository {
        return CommentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetPaginatedPostCommentsUseCase(
        repository: CommentRepository,
    ): GetPaginatedPostCommentsUseCase {
        return GetPaginatedPostCommentsUseCase(repository)
    }
}