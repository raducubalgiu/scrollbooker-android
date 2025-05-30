package com.example.scrollbooker.feature.profile.domain.repository

import com.example.scrollbooker.feature.profile.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(): User
}