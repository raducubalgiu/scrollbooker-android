package com.example.scrollbooker.entity.onboarding.domain.repository
import android.net.Uri
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.onboarding.data.remote.BusinessCreateRequest
import com.example.scrollbooker.entity.onboarding.domain.model.BusinessCreateResponse

interface OnboardingRepository {
    // Shared
    suspend fun collectUserUsername(username: String): AuthState

    // Client
    suspend fun collectClientBirthDate(birthdate: String?): AuthState
    suspend fun collectClientGender(gender: String): AuthState
    suspend fun collectUserLocationPermission(): AuthState

    // Business
    suspend fun collectBusiness(request: BusinessCreateRequest): BusinessCreateResponse
    suspend fun collectBusinessGallery(
        businessId: Int,
        photos: List<Uri?>
    ): AuthState
    suspend fun collectBusinessServices(serviceIds: List<Int>): AuthState
    suspend fun collectBusinessSchedules(schedules: List<Schedule>): AuthState
    suspend fun collectBusinessHasEmployees(hasEmployees: Boolean): AuthState
    suspend fun collectBusinessCurrencies(currencyIds: List<Int>): AuthState
}