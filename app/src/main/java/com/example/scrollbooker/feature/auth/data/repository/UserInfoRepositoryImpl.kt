package com.example.scrollbooker.feature.auth.data.repository

import com.example.scrollbooker.feature.auth.data.mappers.toDomain
import com.example.scrollbooker.feature.auth.data.remote.UserInfoApiService
import com.example.scrollbooker.feature.auth.domain.model.UserInfo
import com.example.scrollbooker.feature.auth.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val apiService: UserInfoApiService
): UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo {
        return apiService.getUserInfo().toDomain()
    }
}