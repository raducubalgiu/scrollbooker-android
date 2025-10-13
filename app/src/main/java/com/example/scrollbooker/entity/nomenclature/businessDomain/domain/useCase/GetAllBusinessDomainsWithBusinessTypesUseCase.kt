package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository.BusinessDomainRepository
import timber.log.Timber
import java.lang.Exception

class GetAllBusinessDomainsWithBusinessTypesUseCase(
    private val repository: BusinessDomainRepository
) {
    suspend operator fun invoke(): FeatureState<List<BusinessDomainsWithBusinessTypes>> {
        return try {
            val businessDomainsWithBusinessTypes = repository.getAllBusinessDomainsWithBusinessTypes()
            FeatureState.Success(businessDomainsWithBusinessTypes)
        } catch (e: Exception) {
            Timber.tag("Business Domains").e("ERROR: on Fetching Business Domains with Business types: $e")
            FeatureState.Error(e)
        }
    }
}