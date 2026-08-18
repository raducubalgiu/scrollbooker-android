package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository

class GetAllServiceDomainsUseCase(
    private val repository: ServiceDomainRepository
) {
    suspend operator fun invoke(): Result<List<ServiceDomain>> {
        return runSuspendCatching {
            repository.getAllServiceDomains()
        }
    }
}