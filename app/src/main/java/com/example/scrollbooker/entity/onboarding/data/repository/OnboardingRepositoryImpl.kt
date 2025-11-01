package com.example.scrollbooker.entity.onboarding.data.repository

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessHasEmployeesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDto
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.nomenclature.currency.data.remote.UserCurrencyUpdateRequest
import com.example.scrollbooker.entity.onboarding.data.remote.OnboardingApiService
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateGenderRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateUsernameRequest
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val apiService: OnboardingApiService
): OnboardingRepository {
    override suspend fun collectUserUsername(username: String): AuthState {
        val request = UpdateUsernameRequest(username)
        return apiService.collectUserUsername(request).toDomain()
    }

    override suspend fun collectClientBirthDate(birthdate: String?): AuthState {
        val request = UpdateBirthDateRequest(birthdate)
        return apiService.collectClientBirthDate(request).toDomain()
    }

    override suspend fun collectClientGender(gender: String): AuthState {
        val request = UpdateGenderRequest(gender)
        return apiService.collectClientGender(request).toDomain()
    }

    override suspend fun collectUserLocationPermission(): AuthState {
        return apiService.collectUserLocationPermission().toDomain()
    }

    override suspend fun collectBusinessServices(serviceIds: List<Int>): AuthState {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.collectBusinessServices(request).toDomain()
    }

    override suspend fun collectBusinessSchedules(schedules: List<Schedule>): AuthState {
        return apiService.collectBusinessSchedules(schedules.toDto()).toDomain()
    }

    override suspend fun collectBusinessHasEmployees(hasEmployees: Boolean): AuthState {
        val request = BusinessHasEmployeesUpdateRequest(hasEmployees)

        return apiService.collectBusinessHasEmployees(request).toDomain()
    }

    override suspend fun collectBusinessCurrencies(currencyIds: List<Int>): AuthState {
        val request = UserCurrencyUpdateRequest(currencyIds)

        return apiService.collectBusinessCurrencies(request).toDomain()
    }
}