package com.example.scrollbooker.feature.auth.domain.usecase

import com.example.scrollbooker.feature.auth.data.mappers.toDomain
import com.example.scrollbooker.feature.auth.data.remote.UserApiService
import com.example.scrollbooker.feature.auth.domain.model.User
import com.example.scrollbooker.feature.auth.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userApi: UserApiService
) {
    suspend operator fun invoke(): User {
        return userApi.getUserInfo().toDomain()
    }
}