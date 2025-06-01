package com.example.scrollbooker.feature.myBusiness.domain.useCase

import com.example.scrollbooker.feature.myBusiness.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.domain.repository.ServiceRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): Result<List<Service>> {
        return try {
            val userId = authDataStore.getUserId().firstOrNull()
                ?: return Result.failure(IllegalStateException("User id Not Found"))

            val services = serviceRepository.getServices(userId)
            Result.success(services)
        } catch (e: Exception) {
            Timber.tag("Services").e(e, "ERROR: on Loading Services")
            Result.failure(e)
        }
    }
}