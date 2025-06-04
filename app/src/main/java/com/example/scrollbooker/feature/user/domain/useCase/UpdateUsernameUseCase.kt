package com.example.scrollbooker.feature.user.domain.useCase

import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String): Result<Unit> = runCatching {
        userRepository.updateUsername(username)
    }
}