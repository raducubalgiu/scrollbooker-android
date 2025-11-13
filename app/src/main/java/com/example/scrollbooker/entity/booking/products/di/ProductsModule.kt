package com.example.scrollbooker.entity.booking.products.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.booking.products.data.remote.ProductsApiService
import com.example.scrollbooker.entity.booking.products.data.repository.ProductRepositoryImpl
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductByIdUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByAppointmentIdUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
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
    fun provideGetProductByIdUseCase(
        repository: ProductRepository,
    ): GetProductByIdUseCase {
        return GetProductByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductsByUserIdUseCase(
        repository: ProductRepository,
    ): GetProductsByUserIdAndServiceIdUseCase {
        return GetProductsByUserIdAndServiceIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateProductUseCase(
        repository: ProductRepository,
    ): CreateProductUseCase {
        return CreateProductUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteProductUseCase(
        repository: ProductRepository,
    ): DeleteProductUseCase {
        return DeleteProductUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductsByAppointmentIdUseCase(
        repository: ProductRepository,
    ): GetProductsByAppointmentIdUseCase {
        return GetProductsByAppointmentIdUseCase(repository)
    }
}