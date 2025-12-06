package com.example.scrollbooker.entity.nomenclature.filter.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.repository.FilterRepository
import timber.log.Timber
import javax.inject.Inject

class GetFiltersByServiceUseCase @Inject constructor(
    private val repository: FilterRepository,
) {
    suspend operator fun invoke(serviceId: Int): FeatureState<List<Filter>> {
        return try {
            val response = repository.getFiltersByServiceId(serviceId)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Filters").e("ERROR: on Fetching Filters By Service Id $e")
            FeatureState.Error()
        }
    }
}