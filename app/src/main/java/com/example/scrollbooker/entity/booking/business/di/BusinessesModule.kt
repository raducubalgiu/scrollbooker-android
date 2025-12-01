package com.example.scrollbooker.entity.booking.business.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.booking.business.data.repository.BusinessRepositoryImpl
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.booking.business.domain.useCase.CreateBusinessUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByIdUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByUserUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessLocationUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesMarkersUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.SearchBusinessAddressUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessHasEmployeesUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessServicesUseCase
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
    fun provideGetBusinessByIdUseCase(
        repository: BusinessRepository,
    ): GetBusinessByIdUseCase {
        return GetBusinessByIdUseCase(repository)
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
    fun provideGetBusinessLocationUseCase(
        repository: BusinessRepository,
    ): GetBusinessLocationUseCase {
        return GetBusinessLocationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBusinessHasEmployeesUseCase(
        repository: BusinessRepository,
    ): UpdateBusinessHasEmployeesUseCase {
        return UpdateBusinessHasEmployeesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateBusinessUseCase(
        repository: BusinessRepository,
    ): CreateBusinessUseCase {
        return CreateBusinessUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBusinessesMarkersUseCase(
        repository: BusinessRepository,
    ): GetBusinessesMarkersUseCase {
        return GetBusinessesMarkersUseCase(repository)
    }
}