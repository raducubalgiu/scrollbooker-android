package com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.repository.ServiceDomainRepository
import timber.log.Timber
import java.lang.Exception

class GetSelectedServiceDomainsWithServicesByBusinessIdUseCase(
    private val repository: ServiceDomainRepository
) {
    suspend operator fun invoke(businessId: Int): FeatureState<List<SelectedServiceDomainsWithServices>> {
        return try {
            val serviceDomains = repository.getServiceDomainsWithServicesByBusinessDomainId(businessId)
            FeatureState.Success(serviceDomains)
        } catch (e: Exception) {
            Timber.tag("Service Domains").e(e, "ERROR: on Fetching ServiceDomains With Services By Business Id")
            FeatureState.Error(e)
        }
    }
}