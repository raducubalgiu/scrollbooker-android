package com.example.scrollbooker.entity.user.userProfile.domain.usecase
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(username: String): Result<Unit> = runCatching {
        userRepository.updateUsername(username)
    }
}