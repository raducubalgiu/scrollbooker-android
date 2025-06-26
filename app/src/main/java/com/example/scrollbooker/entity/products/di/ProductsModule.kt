package com.example.scrollbooker.entity.products.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.products.data.remote.ProductsApiService
import com.example.scrollbooker.entity.products.data.repository.ProductRepositoryImpl
import com.example.scrollbooker.entity.products.domain.repository.ProductRepository
import com.example.scrollbooker.entity.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
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
object ProductsModule {
    @Provides
    @Singleton
    fun provideProductsApiService(okHttpClient: OkHttpClient): ProductsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(apiService: ProductsApiService): ProductRepository {
        return ProductRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetProductsByUserIdUseCase(
        repository: ProductRepository,
    ): GetProductsByUserIdAndServiceIdUseCase {
        return GetProductsByUserIdAndServiceIdUseCase(repository)
    }
}