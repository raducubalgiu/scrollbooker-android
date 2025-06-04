package com.example.scrollbooker.feature.user.domain.useCase

import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UpdateBioUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(bio: String): Result<Unit> = runCatching {
        userRepository.updateBio(bio)
    }
}