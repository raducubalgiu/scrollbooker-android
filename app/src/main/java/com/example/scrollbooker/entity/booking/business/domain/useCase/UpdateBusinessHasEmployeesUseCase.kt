package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class UpdateBusinessHasEmployeesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(hasEmployees: Boolean): Result<AuthState> = runCatching {
        repository.updateBusinessHasEmployees(hasEmployees)
    }
}