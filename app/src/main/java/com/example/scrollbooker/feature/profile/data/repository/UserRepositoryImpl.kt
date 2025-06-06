package com.example.scrollbooker.feature.profile.data.repository

import com.example.scrollbooker.feature.profile.data.mappers.toDomain
import com.example.scrollbooker.feature.profile.data.remote.UserApiService
import com.example.scrollbooker.feature.profile.domain.model.User
import com.example.scrollbooker.feature.profile.domain.repository.UserRepository
import javax.inject.Inject
import kotlin.collections.mapOf

class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService
): UserRepository {
    override suspend fun getUserInfo(): User {
        return api.getUserInfo().toDomain()
    }

    override suspend fun updateUsername(username: String): User {
        val body = mapOf("username" to username)
        return api.updateUsername(body).toDomain()
    }

    override suspend fun updateFullName(fullName: String): User {
        val body = mapOf("fullName" to fullName)
        return api.updateFullName(body).toDomain()
    }

}