package com.example.scrollbooker.entity.location.di

import com.example.scrollbooker.entity.location.data.LocationRepository
import com.example.scrollbooker.entity.location.data.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}