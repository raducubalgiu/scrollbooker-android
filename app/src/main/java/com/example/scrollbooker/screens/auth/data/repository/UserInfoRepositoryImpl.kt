package com.example.scrollbooker.screens.auth.data.repository
import com.example.scrollbooker.screens.auth.data.mappers.toDomain
import com.example.scrollbooker.screens.auth.data.remote.userInfo.UserInfoApiService
import com.example.scrollbooker.screens.auth.domain.model.UserInfo
import com.example.scrollbooker.screens.auth.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val apiService: UserInfoApiService
): UserInfoRepository {
    override suspend fun getUserInfo(): UserInfo {
        return apiService.getUserInfo().toDomain()
    }
}