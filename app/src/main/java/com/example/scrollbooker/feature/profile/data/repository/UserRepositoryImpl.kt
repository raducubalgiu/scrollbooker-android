package com.example.scrollbooker.feature.profile.data.repository

import android.util.Log
import com.example.scrollbooker.feature.profile.data.mappers.toDomain
import com.example.scrollbooker.feature.profile.data.remote.UserApiService
import com.example.scrollbooker.feature.profile.domain.model.User
import com.example.scrollbooker.feature.profile.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
): UserRepository {
    override suspend fun getUserInfo(): User {
        return userApiService.getUserInfo().toDomain()
    }
}