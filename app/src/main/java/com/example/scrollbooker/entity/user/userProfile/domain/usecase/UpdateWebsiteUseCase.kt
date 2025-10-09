package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateWebsiteUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(website: String): Result<Unit> = runCatching {
        userRepository.updateWebsite(website)
    }
}