package com.example.scrollbooker.entity.profession.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.profession.data.remote.ProfessionsApiService
import com.example.scrollbooker.entity.profession.data.repository.ProfessionRepositoryImpl
import com.example.scrollbooker.entity.profession.domain.repository.ProfessionRepository
import com.example.scrollbooker.entity.profession.domain.useCase.GetProfessionsByBusinessTypeUseCase
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
object ProfessionsModule {
    @Provides
    @Singleton
    fun provideProfessionsApiService(okHttpClient: OkHttpClient): ProfessionsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfessionsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfessionsRepository(apiService: ProfessionsApiService): ProfessionRepository {
        return ProfessionRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProfessionsByBusinessTypeUseCase(
        repository: ProfessionRepository,
    ): GetProfessionsByBusinessTypeUseCase {
        return GetProfessionsByBusinessTypeUseCase(repository)
    }
}