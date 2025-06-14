package com.example.scrollbooker.feature.profile.domain.usecase

import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateBioUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(bio: String): Result<Unit> = runCatching {
        userRepository.updateBio(bio)
    }
}