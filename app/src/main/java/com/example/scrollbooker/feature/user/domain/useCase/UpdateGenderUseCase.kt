package com.example.scrollbooker.feature.user.domain.useCase

import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UpdateGenderUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(gender: String): Result<Unit> = runCatching {
        userRepository.updateGender(gender)
    }
}