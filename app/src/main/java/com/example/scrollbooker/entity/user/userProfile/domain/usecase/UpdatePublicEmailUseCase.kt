package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdatePublicEmailUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(publicEmail: String): Result<Unit> = runCatching {
        userRepository.updatePublicEmail(publicEmail)
    }
}