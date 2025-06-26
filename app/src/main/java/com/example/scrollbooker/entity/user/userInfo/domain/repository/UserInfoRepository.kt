package com.example.scrollbooker.entity.user.userInfo.domain.repository

import com.example.scrollbooker.entity.user.userInfo.domain.model.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo
}