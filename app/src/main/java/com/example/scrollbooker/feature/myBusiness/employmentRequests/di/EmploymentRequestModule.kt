package com.example.scrollbooker.feature.myBusiness.employmentRequests.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote.EmploymentRequestsApiService
import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.repository.EmploymentRequestRepositoryImpl
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.repository.EmploymentRequestRepository
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.useCase.GetEmploymentRequestsUseCase
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
object EmploymentRequestModule {
    @Provides
    @Singleton
    fun provideEmploymentRequestsApiService(okHttpClient: OkHttpClient): EmploymentRequestsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmploymentRequestsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEmploymentRequestRepository(apiService: EmploymentRequestsApiService): EmploymentRequestRepository {
        return EmploymentRequestRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetEmploymentRequestsUseCase(
        authDataStore: AuthDataStore,
        repository: EmploymentRequestRepository,
    ): GetEmploymentRequestsUseCase {
        return GetEmploymentRequestsUseCase(authDataStore, repository)
    }
}