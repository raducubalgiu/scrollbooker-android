package com.example.scrollbooker.entity.user.userInfo.data.repository

import com.example.scrollbooker.entity.user.userInfo.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userInfo.data.remote.UserInfoApiService
import com.example.scrollbooker.entity.user.userInfo.domain.model.UserInfo
import com.example.scrollbooker.entity.user.userInfo.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val apiService: UserInfoApiService
): UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo {
        return apiService.getUserInfo().toDomain()
    }
}