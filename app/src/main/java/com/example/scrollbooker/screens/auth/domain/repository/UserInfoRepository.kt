package com.example.scrollbooker.screens.auth.domain.repository

import com.example.scrollbooker.screens.auth.domain.model.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo
}