package com.example.scrollbooker.entity.booking.business.domain.useCase
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import javax.inject.Inject

class UpdateBusinessServicesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(serviceIds: List<Int>): Result<List<Service>> = runCatching {
        repository.updateBusinessServices(serviceIds)
    }
}