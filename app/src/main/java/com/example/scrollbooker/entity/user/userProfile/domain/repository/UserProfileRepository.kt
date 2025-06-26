package com.example.scrollbooker.entity.user.userProfile.domain.repository

import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile
    suspend fun updateFullName(fullName: String)
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
    suspend fun updateGender(gender: String)
//    suspend fun searchUsersClients(q: String): List<UserSocial>
}