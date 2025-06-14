package com.example.scrollbooker.feature.profile.domain.usecase

import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(username: String): Result<Unit> = runCatching {
        userRepository.updateUsername(username)
    }
}