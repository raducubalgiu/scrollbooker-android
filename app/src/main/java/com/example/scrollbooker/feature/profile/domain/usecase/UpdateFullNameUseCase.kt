package com.example.scrollbooker.feature.profile.domain.usecase
import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateFullNameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(fullName: String): Result<Unit> = runCatching {
        repository.updateFullName(fullName)
    }
}