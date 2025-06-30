package com.example.scrollbooker.entity.user.userProfile.domain.repository
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile
    suspend fun updateFullName(fullName: String)
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
    suspend fun updateBirthDate(birthdate: String?)
    suspend fun updateGender(gender: String?)
    suspend fun searchUsername(username: String): SearchUsernameResponse
//    suspend fun searchUsersClients(q: String): List<UserSocial>
}