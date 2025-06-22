package com.example.scrollbooker.shared.business.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object BusinessesModule {
//    @Provides
//    @Singleton
//    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
//        if(!Places.isInitialized()) {
//            Places.initialize(context, "API_KEY")
//        }
//        return Places.createClient(context)
//    }
//}