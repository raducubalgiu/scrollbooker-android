package com.example.scrollbooker.shared.notification.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationsApiService {
    @GET("notifications/")
    suspend fun getUserNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<NotificationDto>
}