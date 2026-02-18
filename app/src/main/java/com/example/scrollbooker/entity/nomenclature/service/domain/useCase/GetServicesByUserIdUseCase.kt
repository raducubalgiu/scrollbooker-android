package com.example.scrollbooker.entity.nomenclature.service.domain.useCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceDomainWithServices
import com.example.scrollbooker.entity.nomenclature.service.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServicesByUserIdUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(
        userId: Int,
        onlyWithProducts: Boolean = true
    ): Result<List<ServiceDomainWithServices>> {
        return serviceRepository.getServicesByUserId(userId, onlyWithProducts)
    }
}