package com.example.scrollbooker.entity.business.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.business.data.repository.BusinessRepositoryImpl
import com.example.scrollbooker.entity.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.business.domain.useCase.GetBusinessByUserUseCase
import com.example.scrollbooker.entity.business.domain.useCase.SearchBusinessAddressUseCase
import com.example.scrollbooker.entity.business.domain.useCase.UpdateBusinessHasEmployeesUseCase
import com.example.scrollbooker.entity.business.domain.useCase.UpdateBusinessServicesUseCase
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
object BusinessModule {
    @Provides
    @Singleton
    fun provideBusinessApiService(okHttpClient: OkHttpClient): BusinessApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BusinessApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBusinessRepository(apiService: BusinessApiService): BusinessRepository {
        return BusinessRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchBusinessAddressUseCase(
        repository: BusinessRepository,
    ): SearchBusinessAddressUseCase {
        return SearchBusinessAddressUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBusinessServicesUseCase(
        repository: BusinessRepository,
    ): UpdateBusinessServicesUseCase {
        return UpdateBusinessServicesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBusinessByUserIdUseCase(
        authDataStore: AuthDataStore,
        repository: BusinessRepository,
    ): GetBusinessByUserUseCase {
        return GetBusinessByUserUseCase(authDataStore, repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBusinessHasEmployeesUseCase(
        repository: BusinessRepository,
    ): UpdateBusinessHasEmployeesUseCase {
        return UpdateBusinessHasEmployeesUseCase(repository)
    }
}