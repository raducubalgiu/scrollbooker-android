package com.example.scrollbooker.feature.myBusiness.services.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.services.domain.repository.ServiceRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetServicesByBusinessTypeUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): FeatureState<List<Service>> {
        return try {
            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()

            if(businessTypeId == null) {
                Timber.Forest.tag("Services").e("ERROR: Business Type Id not found in DataStore")
                return FeatureState.Error()
            }

            val services = serviceRepository.getServicesByBusinessType(businessTypeId)
            FeatureState.Success(services)
        } catch (e: Exception) {
            Timber.Forest.tag("Services").e(e, "ERROR: on Fetching Services By Business Type Id")
            return FeatureState.Error()
        }
    }
}