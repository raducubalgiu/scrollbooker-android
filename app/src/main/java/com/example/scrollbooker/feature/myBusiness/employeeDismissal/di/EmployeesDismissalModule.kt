package com.example.scrollbooker.feature.myBusiness.employeeDismissal.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.remote.EmployeesDismissalApiService
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.repository.EmployeeDismissalRepositoryImpl
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.repository.EmployeeDismissalRepository
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.useCase.GetEmployeeDismissalUseCase
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
object EmployeesDismissalModule {
    @Provides
    @Singleton
    fun provideEmployeesDismissalApiService(okHttpClient: OkHttpClient): EmployeesDismissalApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployeesDismissalApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEmployeeDismissalRepository(
        api: EmployeesDismissalApiService
    ): EmployeeDismissalRepository {
        return EmployeeDismissalRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetEmployeesDismissalUseCase(
        repository: EmployeeDismissalRepository,
    ): GetEmployeeDismissalUseCase {
        return GetEmployeeDismissalUseCase(repository)
    }
}