package com.example.scrollbooker.entity.social.cloudflare.di
import android.content.Context
import com.example.scrollbooker.BuildConfig
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareApiService
import com.example.scrollbooker.entity.social.cloudflare.data.repository.CloudflareRepositoryImpl
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudflareModule {
    @Provides
    @Singleton
    fun provideCloudflareApiService(okHttpClient: OkHttpClient): CloudflareApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudflareApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudflareRepository(
        apiService: CloudflareApiService,
        @ApplicationContext context: Context
    ): CloudflareRepository {
        return CloudflareRepositoryImpl(apiService ,context)
    }
}