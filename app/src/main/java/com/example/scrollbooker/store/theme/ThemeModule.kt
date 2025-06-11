package com.example.scrollbooker.store.theme

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {
    @Provides
    @Singleton
    fun provideThemeDataStore(
        @ApplicationContext context: Context
    ): ThemeDataStore {
        return ThemeDataStore(context)
    }
}