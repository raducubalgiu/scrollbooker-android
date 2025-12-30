package com.example.scrollbooker.entity.permission.di

import com.example.scrollbooker.entity.permission.data.repository.PermissionRepositoryImpl
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppPermissionsModule {
    @Binds
    @Singleton
    abstract fun providePermissionRepository(
        impl: PermissionRepositoryImpl
    ): PermissionRepository
}