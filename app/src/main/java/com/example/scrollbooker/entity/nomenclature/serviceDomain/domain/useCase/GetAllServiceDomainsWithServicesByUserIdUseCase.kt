package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import javax.inject.Inject

class GetAllServiceDomainsWithServicesByUserIdUseCase @Inject constructor(
    private val repository: ServiceDomainRepository
) {
    suspend operator fun invoke(
        userId: Int,
        onlyWithProducts: Boolean = true,
        withFilters: Boolean = true
    ): Result<ServiceDomainWithEmployeeServicesResponse> {
        return repository.getServiceDomainsWithServicesByUserId(userId, onlyWithProducts, withFilters)
    }
}