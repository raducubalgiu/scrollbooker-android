package com.example.scrollbooker.shared.user.userInfo.domain.repository

import com.example.scrollbooker.shared.user.userInfo.domain.model.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo
}