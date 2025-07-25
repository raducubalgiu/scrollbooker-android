package com.example.scrollbooker.entity.social.comment.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.social.comment.data.remote.CommentsApiService
import com.example.scrollbooker.entity.social.comment.data.repository.CommentRepositoryImpl
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import com.example.scrollbooker.entity.social.comment.domain.useCase.CreateCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.GetPaginatedPostCommentsUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.LikeCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.UnLikeCommentUseCase
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

    @Provides
    @Singleton
    fun provideCreateCommentUseCase(
        repository: CommentRepository,
    ): CreateCommentUseCase {
        return CreateCommentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLikeCommentUseCase(
        repository: CommentRepository,
    ): LikeCommentUseCase {
        return LikeCommentUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUnLikeCommentUseCase(
        repository: CommentRepository,
    ): UnLikeCommentUseCase {
        return UnLikeCommentUseCase(repository)
    }
}