package com.example.scrollbooker.entity.nomenclature.filter.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.repository.FilterRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetFiltersByBusinessTypeUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: FilterRepository
) {
    suspend operator fun invoke(): FeatureState<List<Filter>> {
        return try {
            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()

            if(businessTypeId == null) {
                throw IllegalStateException("Business Type Id not found in AuthDataStore")
            }

            val response = repository.getFiltersByBusinessTypeId(businessTypeId)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Filters").e("ERROR: on Fetching Filters $e")
            FeatureState.Error()
        }
    }
}