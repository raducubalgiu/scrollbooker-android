package com.example.scrollbooker.feature.user.data.repository

import com.example.scrollbooker.feature.auth.data.mappers.toDomain
import com.example.scrollbooker.feature.user.data.mappers.toDomain
import com.example.scrollbooker.feature.user.data.remote.UserApiService
import com.example.scrollbooker.feature.auth.domain.model.Permission
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
): UserRepository {
    override suspend fun getUserInfo(): User {
        return userApiService.getUserInfo().toDomain()
    }

    override suspend fun getUserPermissions(): List<Permission> {
        return userApiService.getUserPermissions().toDomain()
    }
}