package com.example.scrollbooker.entity.nomenclature.filter.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.nomenclature.filter.data.remote.FilterApiService
import com.example.scrollbooker.entity.nomenclature.filter.data.repository.FilterRepositoryImpl
import com.example.scrollbooker.entity.nomenclature.filter.domain.repository.FilterRepository
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByBusinessTypeUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.store.AuthDataStore
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
object FiltersModule {
    @Provides
    @Singleton
    fun provideGetFiltersByBusinessType(okHttpClient: OkHttpClient): FilterApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFilterRepository(apiService: FilterApiService): FilterRepository {
        return FilterRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetFiltersByBusinessTypeUseCase(
        authDataStore: AuthDataStore,
        repository: FilterRepository,
    ): GetFiltersByBusinessTypeUseCase {
        return GetFiltersByBusinessTypeUseCase(authDataStore, repository)
    }

    @Provides
    @Singleton
    fun provideGetFiltersByServiceTypeUseCase(
        repository: FilterRepository,
    ): GetFiltersByServiceUseCase {
        return GetFiltersByServiceUseCase(repository)
    }
}