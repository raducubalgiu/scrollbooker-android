package com.example.scrollbooker.entity.social.post.di
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.social.post.data.remote.PostApiService
import com.example.scrollbooker.entity.social.post.data.repository.PostRepositoryImpl
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserVideoReviewsPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
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
    fun providePostsRepository(
        apiService: PostApiService,
    ): PostRepository {
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
    fun provideGetUserVideoReviewsPostsUseCase(
        repository: PostRepository,
    ): GetUserVideoReviewsPostsUseCase {
        return GetUserVideoReviewsPostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetExplorePostsUseCase(
        repository: PostRepository,
    ): GetExplorePostsUseCase {
        return GetExplorePostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFollowingPostsUseCase(
        repository: PostRepository,
    ): GetFollowingPostsUseCase {
        return GetFollowingPostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLikePostUseCase(
        repository: PostRepository,
    ): LikePostUseCase {
        return LikePostUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUnLikePostUseCase(
        repository: PostRepository,
    ): UnLikePostUseCase {
        return UnLikePostUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideBookmarkPostUseCase(
        repository: PostRepository,
    ): BookmarkPostUseCase {
        return BookmarkPostUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUnBookmarkPostUseCase(
        repository: PostRepository,
    ): UnBookmarkPostUseCase {
        return UnBookmarkPostUseCase(repository)
    }
}