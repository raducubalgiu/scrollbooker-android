package com.example.scrollbooker.entity.onboarding.domain.repository
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

interface OnboardingRepository {
    // Shared
    suspend fun collectUserUsername(username: String): AuthState
    // Client
    suspend fun collectClientBirthDate(birthdate: String?): AuthState
    suspend fun collectClientGender(gender: String): AuthState
    suspend fun collectUserLocationPermission(): AuthState
    // Business
    suspend fun collectBusinessSchedules(schedules: List<Schedule>): AuthState
    suspend fun collectBusinessHasEmployees(hasEmployees: Boolean): AuthState
}