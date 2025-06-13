package com.example.scrollbooker.feature.user.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.Permission
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial

interface UserRepository {
    suspend fun getUserInfo(): User
    suspend fun getUserPermissions(): List<Permission>
    suspend fun updateFullName(fullName: String)
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
    suspend fun updateGender(gender: String)
    suspend fun searchUsersClients(q: String): List<UserSocial>
}