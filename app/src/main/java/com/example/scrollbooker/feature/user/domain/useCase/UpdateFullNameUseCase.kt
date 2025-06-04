package com.example.scrollbooker.feature.user.domain.useCase
import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class UpdateFullNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(fullName: String): Result<Unit> = runCatching {
        userRepository.updateFullName(fullName)
    }
}