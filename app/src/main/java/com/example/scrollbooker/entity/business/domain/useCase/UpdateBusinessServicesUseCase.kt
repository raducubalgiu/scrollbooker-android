package com.example.scrollbooker.entity.business.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.business.domain.repository.BusinessRepository
import javax.inject.Inject

class UpdateBusinessServicesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(serviceIds: List<Int>): Result<AuthState> = runCatching {
        repository.updateBusinessServices(serviceIds)
    }
}