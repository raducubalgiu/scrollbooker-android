package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.entity.user.userProfile.data.remote.UserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateAvatarUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(request: UserAvatarRequest): Result<Unit> = runCatching {
        repository.updateAvatar(request)
    }
}