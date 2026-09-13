package com.example.scrollbooker.entity.calendar.connection.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendarConnectionApiService {
    @GET("integrations/calendar/connections/me")
    suspend fun getCalendarConnection(
        @Query("provider") provider: String = "google_calendar"
    ): Response<CalendarConnectionDto>

    @POST("integrations/calendar/connections/google")
    suspend fun connectGoogleCalendar(
        @Body request: ConnectGoogleCalendarRequest
    ): CalendarConnectionDto

    @DELETE("integrations/calendar/connections/me")
    suspend fun disconnectCalendarConnection(
        @Query("provider") provider: String = "google_calendar"
    )
}
