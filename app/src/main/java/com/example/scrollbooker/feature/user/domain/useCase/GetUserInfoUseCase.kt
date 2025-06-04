package com.example.scrollbooker.feature.user.domain.useCase

import com.example.scrollbooker.feature.user.data.mappers.toDomain
import com.example.scrollbooker.feature.user.data.remote.UserApiService
import com.example.scrollbooker.feature.user.domain.model.User
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userApi: UserApiService
) {
    suspend operator fun invoke(): User {
        return userApi.getUserInfo().toDomain()
    }
}