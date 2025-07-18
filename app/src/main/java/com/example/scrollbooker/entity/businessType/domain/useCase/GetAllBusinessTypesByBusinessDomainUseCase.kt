package com.example.scrollbooker.entity.businessType.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.businessType.domain.repository.BusinessTypeRepository
import timber.log.Timber
import java.lang.Exception

class GetAllBusinessTypesByBusinessDomainUseCase(
    private val repository: BusinessTypeRepository
) {
    suspend operator fun invoke(businessDomainId: Int): FeatureState<List<BusinessType>> {
        return try {
            val businessTypes = repository.getBusinessTypesByBusinessDomain(businessDomainId)
            FeatureState.Success(businessTypes)
        } catch (e: Exception) {
            Timber.tag("Business Types").e("ERROR: on Fetching Business Types By Business Domain: $e")
            FeatureState.Error(e)
        }
    }
}