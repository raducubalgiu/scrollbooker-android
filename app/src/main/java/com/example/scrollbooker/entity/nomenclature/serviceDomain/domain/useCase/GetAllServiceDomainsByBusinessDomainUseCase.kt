package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import timber.log.Timber
import java.lang.Exception

class GetAllServiceDomainsByBusinessDomainUseCase(
    private val repository: ServiceDomainRepository
) {
    suspend operator fun invoke(businessDomainId: Int): FeatureState<List<ServiceDomain>> {
        return try {
            val serviceDomains = repository.getServiceDomainsByBusinessDomain(businessDomainId)
            FeatureState.Success(serviceDomains)
        } catch (e: Exception) {
            Timber.tag("Service Domains").e("ERROR: on Fetching ServiceDomains By Business Domain: $e")
            FeatureState.Error(e)
        }
    }
}