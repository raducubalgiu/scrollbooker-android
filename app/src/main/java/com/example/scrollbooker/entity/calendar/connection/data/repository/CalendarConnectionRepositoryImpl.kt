package com.example.scrollbooker.entity.calendar.connection.data.repository

import com.example.scrollbooker.entity.calendar.connection.data.mappers.toDomain
import com.example.scrollbooker.entity.calendar.connection.data.remote.CalendarConnectionApiService
import com.example.scrollbooker.entity.calendar.connection.data.remote.ConnectGoogleCalendarRequest
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection
import com.example.scrollbooker.entity.calendar.connection.domain.repository.CalendarConnectionRepository
import retrofit2.HttpException
import javax.inject.Inject

class CalendarConnectionRepositoryImpl @Inject constructor(
    private val apiService: CalendarConnectionApiService
): CalendarConnectionRepository {
    override suspend fun getCalendarConnection(businessId: Int): CalendarConnection? {
        val response = apiService.getCalendarConnection(businessId)

        // No connection yet is a valid state, not an error - the backend is expected to
        // respond 404 in that case.
        if (response.code() == 404) return null
        if (!response.isSuccessful) throw HttpException(response)

        return response.body()?.toDomain()
    }

    override suspend fun connectGoogleCalendar(businessId: Int, serverAuthCode: String): CalendarConnection {
        return apiService.connectGoogleCalendar(
            businessId = businessId,
            request = ConnectGoogleCalendarRequest(serverAuthCode = serverAuthCode)
        ).toDomain()
    }

    override suspend fun disconnectCalendarConnection(businessId: Int, connectionId: Int) {
        apiService.disconnectCalendarConnection(businessId = businessId, connectionId = connectionId)
    }
}
