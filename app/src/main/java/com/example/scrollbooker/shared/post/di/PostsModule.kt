package com.example.scrollbooker.shared.post.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.post.data.remote.PostApiService
import com.example.scrollbooker.shared.post.data.repository.PostRepositoryImpl
import com.example.scrollbooker.shared.post.domain.repository.PostRepository
import com.example.scrollbooker.shared.post.domain.useCase.GetBookNowPostsUseCase
import com.example.scrollbooker.shared.post.domain.useCase.GetFollowingPostsUseCase
import com.example.scrollbooker.shared.post.domain.useCase.GetUserPostsUseCase
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
object PostsModule {
    @Provides
    @Singleton
    fun providePostsApiService(okHttpClient: OkHttpClient): PostApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePostsRepository(apiService: PostApiService): PostRepository {
        return PostRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserPostsUseCase(
        repository: PostRepository,
    ): GetUserPostsUseCase {
        return GetUserPostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBookNowPostsUseCase(
        repository: PostRepository,
    ): GetBookNowPostsUseCase {
        return GetBookNowPostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFollowingPostsUseCase(
        repository: PostRepository,
    ): GetFollowingPostsUseCase {
        return GetFollowingPostsUseCase(repository)
    }
}