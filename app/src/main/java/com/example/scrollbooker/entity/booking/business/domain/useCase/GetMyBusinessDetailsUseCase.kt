package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessDetails
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class GetMyBusinessDetailsUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(): Result<BusinessDetails> {
        return runSuspendCatching {
            repository.getMyBusinessDetails()
        }
    }
}