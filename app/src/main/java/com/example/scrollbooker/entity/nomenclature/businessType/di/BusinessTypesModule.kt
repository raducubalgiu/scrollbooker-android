package com.example.scrollbooker.entity.nomenclature.businessType.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.nomenclature.businessType.data.remote.BusinessTypeApiService
import com.example.scrollbooker.entity.nomenclature.businessType.data.repository.BusinessTypeRepositoryImpl
import com.example.scrollbooker.entity.nomenclature.businessType.domain.repository.BusinessTypeRepository
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllAvailableBusinessTypesUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllPaginatedBusinessTypesUseCase
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
object BusinessTypesModule {
    @Provides
    @Singleton
    fun provideBusinessTypeApiService(okHttpClient: OkHttpClient): BusinessTypeApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BusinessTypeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBusinessTypeRepository(apiService: BusinessTypeApiService): BusinessTypeRepository {
        return BusinessTypeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAllBusinessTypesUseCase(
        repository: BusinessTypeRepository,
    ): GetAllBusinessTypesUseCase {
        return GetAllBusinessTypesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllAvailableBusinessTypesUseCase(
        repository: BusinessTypeRepository,
    ): GetAllAvailableBusinessTypesUseCase {
        return GetAllAvailableBusinessTypesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllPaginatedBusinessTypesUseCase(
        repository: BusinessTypeRepository,
    ): GetAllPaginatedBusinessTypesUseCase {
        return GetAllPaginatedBusinessTypesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllBusinessTypesByBusinessDomainUseCase(
        repository: BusinessTypeRepository,
    ): GetAllBusinessTypesByBusinessDomainUseCase {
        return GetAllBusinessTypesByBusinessDomainUseCase(repository)
    }
}