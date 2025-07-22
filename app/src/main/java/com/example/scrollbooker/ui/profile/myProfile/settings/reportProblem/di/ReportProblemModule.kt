package com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.data.remote.ReportProblemApiService
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.data.repository.ReportProblemRepositoryImpl
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.domain.repository.ReportProblemRepository
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
object ServicesModule {
    @Provides
    @Singleton
    fun provideReportProblemApiService(okHttpClient: OkHttpClient): ReportProblemApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReportProblemApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportProblemRepository(apiService: ReportProblemApiService): ReportProblemRepository {
        return ReportProblemRepositoryImpl(apiService)
    }
}