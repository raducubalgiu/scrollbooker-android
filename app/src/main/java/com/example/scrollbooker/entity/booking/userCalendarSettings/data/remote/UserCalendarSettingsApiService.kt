package com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserCalendarSettingsApiService {
    @GET("users/{userId}/calendar-settings")
    suspend fun getCalendarSettings(
        @Path("userId") userId: Int
    ): UserCalendarSettingsDto

    @PATCH("calendar-settings/slot-duration")
    suspend fun updateSlotDuration(
        @Body request: SlotDurationUpdateRequest
    ): UserCalendarSettingsDto

    @PATCH("calendar-settings/gap")
    suspend fun updateAppointmentGap(
        @Body request: AppointmentGapUpdateRequest
    ): UserCalendarSettingsDto
}
