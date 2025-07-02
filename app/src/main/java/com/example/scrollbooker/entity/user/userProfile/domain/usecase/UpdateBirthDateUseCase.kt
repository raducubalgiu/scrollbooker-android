package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateBirthDateUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(birthdate: String?): Result<AuthState> = runCatching {
        repository.updateBirthDate(birthdate)
    }
}