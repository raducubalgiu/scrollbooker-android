package com.example.scrollbooker.screens.profile.userSocial.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.screens.profile.userSocial.data.remote.UserSocialApiService
import com.example.scrollbooker.screens.profile.userSocial.data.repository.UserSocialRepositoryImpl
import com.example.scrollbooker.screens.profile.userSocial.domain.repository.UserSocialRepository
import com.example.scrollbooker.screens.profile.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.screens.profile.userSocial.domain.useCase.GetUserSocialFollowersUseCase
import com.example.scrollbooker.screens.profile.userSocial.domain.useCase.GetUserSocialFollowingsUseCase
import com.example.scrollbooker.screens.profile.userSocial.domain.useCase.UnfollowUserUseCase
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
object UserSocialModule {
    @Provides
    @Singleton
    fun provideUserSocialApiService(okHttpClient: OkHttpClient): UserSocialApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserSocialApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserSocialRepository(
        api: UserSocialApiService
    ): UserSocialRepository {
        return UserSocialRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetUserSocialFollowersUseCase(
        repository: UserSocialRepository,
    ): GetUserSocialFollowersUseCase {
        return GetUserSocialFollowersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserSocialFollowingsUseCase(
        repository: UserSocialRepository,
    ): GetUserSocialFollowingsUseCase {
        return GetUserSocialFollowingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFollowUserUseCase(
        repository: UserSocialRepository,
    ): FollowUserUseCase {
        return FollowUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUnfollowUserUseCase(
        repository: UserSocialRepository,
    ): UnfollowUserUseCase {
        return UnfollowUserUseCase(repository)
    }
}