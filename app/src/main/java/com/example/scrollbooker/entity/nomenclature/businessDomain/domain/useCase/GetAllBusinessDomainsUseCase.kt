package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository.BusinessDomainRepository
import timber.log.Timber
import java.lang.Exception

class GetAllBusinessDomainsUseCase(
    private val repository: BusinessDomainRepository
) {
    suspend operator fun invoke(): FeatureState<List<BusinessDomain>> {
        return try {
            val businessDomains = repository.getBusinessDomains()
            FeatureState.Success(businessDomains)
        } catch (e: Exception) {
            Timber.tag("Business Domains").e("ERROR: on Fetching Business Domains: $e")
            FeatureState.Error(e)
        }
    }
}