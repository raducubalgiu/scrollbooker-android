package com.example.scrollbooker.feature.profile.domain.repository

import com.example.scrollbooker.feature.profile.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile
}