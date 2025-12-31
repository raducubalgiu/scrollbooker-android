package com.example.scrollbooker.entity.booking.employee.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.employee.data.remote.EmployeesApiService
import com.example.scrollbooker.entity.booking.employee.data.repository.EmployeesRepositoryImpl
import com.example.scrollbooker.entity.booking.employee.domain.repository.EmployeesRepository
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesUseCase
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
object EmployeesModule {
    @Provides
    @Singleton
    fun provideEmployeesApiService(okHttpClient: OkHttpClient): EmployeesApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployeesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEmployeesRepository(apiService: EmployeesApiService): EmployeesRepository {
        return EmployeesRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetEmployeesUseCase(
        authDataStore: AuthDataStore,
        repository: EmployeesRepository,
    ): GetEmployeesUseCase {
        return GetEmployeesUseCase(authDataStore, repository)
    }

    @Provides
    @Singleton
    fun provideGetEmployeesByOwnerUseCase(
        repository: EmployeesRepository,
    ): GetEmployeesByOwnerUseCase {
        return GetEmployeesByOwnerUseCase(repository)
    }
}