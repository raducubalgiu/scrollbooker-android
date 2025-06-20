package com.example.scrollbooker.shared.businessType.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.shared.businessType.data.remote.BusinessTypeApiService
import com.example.scrollbooker.shared.businessType.data.repository.BusinessTypeRepository
import com.example.scrollbooker.shared.businessType.domain.repository.BusinessTypeRepositoryImpl
import com.example.scrollbooker.shared.businessType.domain.useCase.GetAllBusinessTypesUseCase
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
    fun provideBusinessTypeApiService(): BusinessTypeApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            //.client(okHttpClient)
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
}