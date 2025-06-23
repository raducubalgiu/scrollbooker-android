package com.example.scrollbooker.shared.user.userProfile.di

import com.example.scrollbooker.core.util.Constants.BASE_URL
import com.example.scrollbooker.shared.user.userProfile.data.remote.UserProfileApiService
import com.example.scrollbooker.shared.user.userProfile.data.repository.UserProfileRepositoryImpl
import com.example.scrollbooker.shared.user.userProfile.domain.repository.UserProfileRepository
import com.example.scrollbooker.shared.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.shared.user.userProfile.domain.usecase.UpdateFullNameUseCase
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
object UserProfileModule {
    @Provides
    @Singleton
    fun provideUserProfileApiService(okHttpClient: OkHttpClient): UserProfileApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        apiService: UserProfileApiService
    ): UserProfileRepository = UserProfileRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(
        repository: UserProfileRepository
    ): GetUserProfileUseCase {
        return GetUserProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateFullNameUseCase(
        repository: UserProfileRepository
    ): UpdateFullNameUseCase {
        return UpdateFullNameUseCase(repository)
    }
}