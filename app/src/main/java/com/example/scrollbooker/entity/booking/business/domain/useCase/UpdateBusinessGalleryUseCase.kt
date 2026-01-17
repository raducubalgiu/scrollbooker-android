package com.example.scrollbooker.entity.booking.business.domain.useCase

import android.net.Uri
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class UpdateBusinessGalleryUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        photos: List<Uri?>
    ): Result<Unit> = runCatching {
        repository.updateBusinessGallery(businessId, photos)
    }
}