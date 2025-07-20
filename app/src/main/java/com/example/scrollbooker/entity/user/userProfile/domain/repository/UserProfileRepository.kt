package com.example.scrollbooker.entity.user.userProfile.domain.repository
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile
    suspend fun updateFullName(fullName: String)

    suspend fun updateUsername(username: String): AuthState
    suspend fun updateBirthDate(birthdate: String?): AuthState
    suspend fun updateGender(gender: String): AuthState

    suspend fun updateBio(bio: String)
    suspend fun searchUsername(username: String): SearchUsernameResponse
}