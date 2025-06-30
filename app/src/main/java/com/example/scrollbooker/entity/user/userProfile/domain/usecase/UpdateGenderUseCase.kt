package com.example.scrollbooker.entity.user.userProfile.domain.usecase
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateGenderUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(gender: String): Result<Unit> = runCatching {
        userRepository.updateGender(gender)
    }
}