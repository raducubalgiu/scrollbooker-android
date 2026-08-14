package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class ShareBusinessProfileUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businessId: Int, channel: ShareChannelEnum): Result<Unit> {
        return try {
            repository.shareBusinessProfile(businessId, channel)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Share Business Profile").e(e, "ERROR: on Sharing Business Profile")
            Result.failure(e)
        }
    }
}