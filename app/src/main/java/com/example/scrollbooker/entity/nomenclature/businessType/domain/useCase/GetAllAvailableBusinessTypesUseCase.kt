package com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.repository.BusinessTypeRepository
import timber.log.Timber
import java.lang.Exception

class GetAllAvailableBusinessTypesUseCase(
    private val repository: BusinessTypeRepository
) {
    suspend operator fun invoke(): FeatureState<List<BusinessType>> {
        return try {
            val businessTypes = repository.getAllAvailableBusinessTypes()
            FeatureState.Success(businessTypes)
        } catch (e: Exception) {
            Timber.tag("Business Types").e("ERROR: on Fetching Available Business Types: $e")
            FeatureState.Error(e)
        }
    }
}