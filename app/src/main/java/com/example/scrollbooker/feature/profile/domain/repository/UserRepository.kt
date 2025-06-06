package com.example.scrollbooker.feature.profile.domain.repository

import com.example.scrollbooker.feature.profile.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(): User
    suspend fun updateUsername(username: String): User
    suspend fun updateFullName(fullName: String): User
}