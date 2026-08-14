package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import timber.log.Timber
import javax.inject.Inject

class ShareUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: Int, channel: ShareChannelEnum): Result<Unit> {
        return try {
            repository.shareUserProfile(userId, channel)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Share User Profile").e(e, "ERROR: on Sharing User Profile")
            Result.failure(e)
        }
    }
}