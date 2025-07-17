package com.example.scrollbooker.entity.businessDomain.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.businessDomain.data.remote.BusinessDomainApiService
import com.example.scrollbooker.entity.businessDomain.data.repository.BusinessDomainRepositoryImpl
import com.example.scrollbooker.entity.businessDomain.domain.repository.BusinessDomainRepository
import com.example.scrollbooker.entity.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
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
    fun provideBusinessDomainApiService(okHttpClient: OkHttpClient): BusinessDomainApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BusinessDomainApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBusinessDomainRepository(apiService: BusinessDomainApiService): BusinessDomainRepository {
        return BusinessDomainRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAllBusinessDomainsUseCase(
        repository: BusinessDomainRepository,
    ): GetAllBusinessDomainsUseCase {
        return GetAllBusinessDomainsUseCase(repository)
    }
}