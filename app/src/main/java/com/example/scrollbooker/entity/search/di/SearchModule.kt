package com.example.scrollbooker.entity.search.di

import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.search.data.remote.SearchApiService
import com.example.scrollbooker.entity.search.data.repository.SearchRepositoryImpl
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import com.example.scrollbooker.entity.search.domain.useCase.GetRecentSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.SearchUsersUseCase
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
object SearchModule {
    @Provides
    @Singleton
    fun provideSearchApiService(okHttpClient: OkHttpClient): SearchApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(apiService: SearchApiService): SearchRepository {
        return SearchRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchUsersUseCase(
        repository: SearchRepository,
    ): SearchUsersUseCase {
        return SearchUsersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRecentSearchUseCase(
        repository: SearchRepository,
    ): GetRecentSearchUseCase {
        return GetRecentSearchUseCase(repository)
    }
}