package com.example.scrollbooker.entity.onboarding.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessHasEmployeesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateGenderRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateUsernameRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface OnboardingApiService {
    // Shared
    @PATCH("/onboarding/collect-user-username")
    suspend fun collectUserUsername(
        @Body request: UpdateUsernameRequest
    ): AuthStateDto

    @PATCH("/onboarding/collect-user-location-permission")
    suspend fun collectUserLocationPermission(): AuthStateDto

    // Client
    @PATCH("/onboarding/collect-client-birthdate")
    suspend fun collectClientBirthDate(
        @Body request: UpdateBirthDateRequest
    ): AuthStateDto

    @PATCH("/onboarding/collect-client-gender")
    suspend fun collectClientGender(
        @Body request: UpdateGenderRequest
    ): AuthStateDto

    // Business
    @PATCH("/onboarding/collect-business-services")
    suspend fun collectBusinessServices(
        @Body request: BusinessServicesUpdateRequest
    ): AuthStateDto

    @PATCH("/onboarding/collect-business-schedules")
    suspend fun collectBusinessSchedules(
        @Body schedules: List<ScheduleDto>
    ): AuthStateDto

    @PATCH("/onboarding/collect-business-has-employees")
    suspend fun collectBusinessHasEmployees(
        @Body request: BusinessHasEmployeesUpdateRequest
    ): AuthStateDto
}