package com.example.scrollbooker.feature.auth.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo
}