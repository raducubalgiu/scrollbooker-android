package com.example.scrollbooker.entity.mapbbox.di
import com.example.scrollbooker.entity.mapbbox.data.remote.MapboxApiService
import com.example.scrollbooker.entity.mapbbox.data.repository.MapboxRepositoryImpl
import com.example.scrollbooker.entity.mapbbox.domain.repository.MapboxRepository
import com.example.scrollbooker.entity.mapbbox.domain.useCase.SearchAddressUseCase
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
object MapboxModule {
    @Provides
    @Singleton
    fun provideConsentsApiService(okHttpClient: OkHttpClient): MapboxApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.mapbox.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapboxApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(
        apiService: MapboxApiService
    ): MapboxRepository {
        return MapboxRepositoryImpl(
            apiService
        )
    }

    @Provides
    @Singleton
    fun provideSearchAddressUseCase(
        repository: MapboxRepository,
    ): SearchAddressUseCase {
        return SearchAddressUseCase(repository)
    }
}