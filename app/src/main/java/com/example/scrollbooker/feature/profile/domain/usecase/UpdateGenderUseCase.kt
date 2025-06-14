package com.example.scrollbooker.feature.profile.domain.usecase

import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateGenderUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
//    suspend operator fun invoke(gender: String): Result<Unit> = runCatching {
//        userRepository.updateGender(gender)
//    }
}