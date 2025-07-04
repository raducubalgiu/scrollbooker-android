package com.example.scrollbooker.entity.currency.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.entity.currency.data.repository.CurrencyRepositoryImpl
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.entity.currency.domain.useCase.GetAllCurrenciesUseCase
import com.example.scrollbooker.entity.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.currency.domain.useCase.UpdateUserCurrenciesUseCase
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
object CurrenciesModule {
    @Provides
    @Singleton
    fun provideCurrenciesApiService(okHttpClient: OkHttpClient): CurrenciesApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrenciesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(apiService: CurrenciesApiService): CurrencyRepository {
        return CurrencyRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAllCurrenciesUseCase(
        repository: CurrencyRepository,
    ): GetAllCurrenciesUseCase {
        return GetAllCurrenciesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserCurrenciesUseCase(
        authDataStore: AuthDataStore,
        repository: CurrencyRepository,
    ): GetUserCurrenciesUseCase {
        return GetUserCurrenciesUseCase(authDataStore, repository)
    }

    @Provides
    @Singleton
    fun provideUserCurrenciesUpdateUseCase(
        repository: CurrencyRepository,
    ): UpdateUserCurrenciesUseCase {
        return UpdateUserCurrenciesUseCase(repository)
    }
}