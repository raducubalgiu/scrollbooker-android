package com.example.scrollbooker.feature.myBusiness.services.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.services.domain.repository.ServiceRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class DetachServiceUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: ServiceRepository
) {
    suspend operator fun invoke(serviceId: Int): FeatureState<Unit> {
        return try {
            val businessId = authDataStore.getBusinessId().firstOrNull()

            if(businessId == null) {
                Timber.Forest.tag("Services").e("ERROR: Business Id Not Found in DataStore")
                FeatureState.Error()
            } else {
                repository.detachService(businessId, serviceId)
                FeatureState.Success(Unit)
            }
        } catch (error: Exception) {
            Timber.Forest.tag("Services").e("ERROR: on Attaching multiple services $error")
            FeatureState.Error(error)
        }
    }
}