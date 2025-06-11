package com.example.scrollbooker.feature.services.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.services.domain.model.Service
import com.example.scrollbooker.feature.services.domain.repository.ServiceRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): FeatureState<List<Service>> {
        return try {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.Forest.tag("Services").e("ERROR: User Id not found in DataStore")
                return FeatureState.Error()
            }

            val services = serviceRepository.getServices(userId)
            FeatureState.Success(services)
        } catch (e: Exception) {
            Timber.Forest.tag("Services").e(e, "ERROR: on Fetching Services")
            return FeatureState.Error()
        }
    }
}