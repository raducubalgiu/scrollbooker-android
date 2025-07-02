package com.example.scrollbooker.entity.user.userProfile.domain.usecase
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(username: String): Result<AuthState> = runCatching {
        userRepository.updateUsername(username)
    }
}