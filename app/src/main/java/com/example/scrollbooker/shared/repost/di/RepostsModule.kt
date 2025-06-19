package com.example.scrollbooker.shared.repost.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.repost.data.remote.RepostsApiService
import com.example.scrollbooker.shared.repost.data.repository.RepostsRepositoryImpl
import com.example.scrollbooker.shared.repost.domain.repository.RepostsRepository
import com.example.scrollbooker.shared.repost.domain.useCase.GetUserRepostsUseCase
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
object RepostsModule {
    @Provides
    @Singleton
    fun provideRepostsApiService(okHttpClient: OkHttpClient): RepostsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RepostsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepostsRepository(apiService: RepostsApiService): RepostsRepository {
        return RepostsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserRepostsUseCase(
        repository: RepostsRepository,
    ): GetUserRepostsUseCase {
        return GetUserRepostsUseCase(repository)
    }
}