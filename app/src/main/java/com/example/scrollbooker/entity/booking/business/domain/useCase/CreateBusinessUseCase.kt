package com.example.scrollbooker.entity.booking.business.domain.useCase
import android.net.Uri
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class CreateBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String,
        photos: List<Uri?>
    ): Result<BusinessCreateResponse> = runCatching {
        repository.createBusiness(
            description = description,
            placeId = placeId,
            businessTypeId = businessTypeId,
            ownerFullName = ownerFullName,
            photos = photos
        )
    }
}