package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository.BusinessDomainRepository

class GetAllBusinessDomainsUseCase(
    private val repository: BusinessDomainRepository
) {
    suspend operator fun invoke(): Result<List<BusinessDomain>> {
        return runSuspendCatching {
            repository.getAllBusinessDomains()
        }
    }
}