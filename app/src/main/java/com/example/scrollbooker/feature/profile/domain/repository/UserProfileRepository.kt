package com.example.scrollbooker.feature.profile.domain.repository

import com.example.scrollbooker.feature.profile.domain.model.UserProfile
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile
    suspend fun updateFullName(fullName: String)
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
//    suspend fun updateGender(gender: String)
//    suspend fun searchUsersClients(q: String): List<UserSocial>
}