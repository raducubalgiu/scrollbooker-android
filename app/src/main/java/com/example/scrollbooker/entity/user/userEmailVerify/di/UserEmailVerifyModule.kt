package com.example.scrollbooker.entity.user.userEmailVerify.di
import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.user.userEmailVerify.domain.useCase.VerifyUserEmailUseCase
import com.example.scrollbooker.entity.user.userEmailVerify.data.remote.UserEmailVerifyApiService
import com.example.scrollbooker.entity.user.userEmailVerify.data.repository.UserEmailVerifyRepositoryImpl
import com.example.scrollbooker.entity.user.userEmailVerify.domain.repository.UserEmailVerifyRepository
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
object UserEmailVerifyModule {
    @Provides
    @Singleton
    fun provideUserEmailVerifyApiService(okHttpClient: OkHttpClient): UserEmailVerifyApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserEmailVerifyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserEmailVerifyRepository(apiService: UserEmailVerifyApiService): UserEmailVerifyRepository {
        return UserEmailVerifyRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideUserEmailEmailVerifyUseCase(
        authDataStore: AuthDataStore,
        repository: UserEmailVerifyRepository,
    ): VerifyUserEmailUseCase {
        return VerifyUserEmailUseCase(authDataStore, repository)
    }
}