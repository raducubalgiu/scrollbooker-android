package com.example.scrollbooker.entity.social.cloudflare.di
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareApiService
import com.example.scrollbooker.entity.social.cloudflare.data.repository.CloudflareRepositoryImpl
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import com.example.scrollbooker.entity.social.cloudflare.domain.useCase.CreatePostWithCloudflareUseCase
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
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
object CloudflareModule {
    @Provides
    @Singleton
    fun provideCloudflareApiService(okHttpClient: OkHttpClient): CloudflareApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudflareApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudflareRepository(
        apiService: CloudflareApiService,
    ): CloudflareRepository {
        return CloudflareRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCreatePostWithCloudflareUseCase(
        cloudflareRepository: CloudflareRepository,
        postsRepository: PostRepository
    ): CreatePostWithCloudflareUseCase {
        return CreatePostWithCloudflareUseCase(cloudflareRepository, postsRepository)
    }
}