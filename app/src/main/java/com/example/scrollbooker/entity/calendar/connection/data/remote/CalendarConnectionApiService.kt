package com.example.scrollbooker.entity.calendar.connection.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Endpoints assumed on the backend, matching the existing `businesses/{businessId}/availability`
 * convention - adjust paths here once the backend contract is finalized.
 */
interface CalendarConnectionApiService {
    @GET("businesses/{businessId}/calendar-connections")
    suspend fun getCalendarConnection(
        @Path("businessId") businessId: Int
    ): Response<CalendarConnectionDto>

    @POST("businesses/{businessId}/calendar-connections/google")
    suspend fun connectGoogleCalendar(
        @Path("businessId") businessId: Int,
        @Body request: ConnectGoogleCalendarRequest
    ): CalendarConnectionDto

    @DELETE("businesses/{businessId}/calendar-connections/{connectionId}")
    suspend fun disconnectCalendarConnection(
        @Path("businessId") businessId: Int,
        @Path("connectionId") connectionId: Int
    )
}
