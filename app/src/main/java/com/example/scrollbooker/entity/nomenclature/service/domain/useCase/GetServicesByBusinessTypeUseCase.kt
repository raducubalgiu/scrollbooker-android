package com.example.scrollbooker.entity.nomenclature.service.domain.useCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServicesByBusinessTypeUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
) {
    suspend operator fun invoke(businessTypeId: Int): Result<List<Service>> {
        return serviceRepository.getServicesByBusinessType(businessTypeId)
    }
}