package com.example.scrollbooker.entity.user.userProfile.di

import com.example.scrollbooker.core.util.Constants.BASE_URL
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileApiService
import com.example.scrollbooker.entity.user.userProfile.data.repository.UserProfileRepositoryImpl
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.SearchUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBioUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBirthDateUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateFullNameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdatePublicEmailUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateWebsiteUseCase
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
    fun provideGetUserProfileAboutUseCase(
        repository: UserProfileRepository,
        authDataStore: AuthDataStore
    ): GetUserProfileAboutUseCase {
        return GetUserProfileAboutUseCase(repository, authDataStore)
    }

    @Provides
    @Singleton
    fun provideSearchUsernameUseCase(
        repository: UserProfileRepository
    ): SearchUsernameUseCase {
        return SearchUsernameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBioUseCase(
        repository: UserProfileRepository
    ): UpdateBioUseCase {
        return UpdateBioUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateBirthDateUseCase(
        repository: UserProfileRepository
    ): UpdateBirthDateUseCase {
        return UpdateBirthDateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateFullNameUseCase(
        repository: UserProfileRepository
    ): UpdateFullNameUseCase {
        return UpdateFullNameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateGenderUseCase(
        repository: UserProfileRepository
    ): UpdateGenderUseCase {
        return UpdateGenderUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePublicEmailUseCase(
        repository: UserProfileRepository
    ): UpdatePublicEmailUseCase {
        return UpdatePublicEmailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUsernameUseCase(
        repository: UserProfileRepository
    ): UpdateUsernameUseCase {
        return UpdateUsernameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateWebsiteUseCase(
        repository: UserProfileRepository
    ): UpdateWebsiteUseCase {
        return UpdateWebsiteUseCase(repository)
    }
}