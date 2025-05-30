package com.example.scrollbooker.feature.profile.domain.usecase

import com.example.scrollbooker.feature.profile.domain.model.User
import com.example.scrollbooker.feature.profile.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): User {
        return repository.getUserInfo()
    }
}