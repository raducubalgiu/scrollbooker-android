package com.example.scrollbooker.feature.services.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.services.domain.model.Service
import com.example.scrollbooker.feature.services.domain.repository.ServiceRepository
import timber.log.Timber
import javax.inject.Inject

class GetServicesByBusinessTypeUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
) {
    suspend operator fun invoke(businessTypeId: Int): FeatureState<List<Service>> {
        return try {
            val services = serviceRepository.getServicesByBusinessType(businessTypeId)
            FeatureState.Success(services)
        } catch (e: Exception) {
            Timber.Forest.tag("Services").e(e, "ERROR: on Fetching Services By Business Type Id")
            return FeatureState.Error()
        }
    }
}