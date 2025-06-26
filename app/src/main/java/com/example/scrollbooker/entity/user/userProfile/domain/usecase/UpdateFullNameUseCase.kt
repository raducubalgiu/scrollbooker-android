package com.example.scrollbooker.entity.user.userProfile.domain.usecase
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateFullNameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(fullName: String): Result<Unit> = runCatching {
        repository.updateFullName(fullName)
    }
}