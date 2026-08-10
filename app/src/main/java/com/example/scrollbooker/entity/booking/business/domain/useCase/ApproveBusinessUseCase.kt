package com.example.scrollbooker.entity.booking.business.domain.useCase
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class ApproveBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(userId: Int): Result<Unit> = runCatching {
        repository.approveBusiness(userId)
    }
}