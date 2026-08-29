package com.example.scrollbooker.entity.user.notification.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationsApiService {
    @GET("notifications/count")
    suspend fun getUserNotificationsNumber(): Int

    @GET("notifications")
    suspend fun getUserNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<NotificationDto>

    @PATCH("notifications/read")
    suspend fun markNotificationAsRead(
        @Body request: MarkNotificationsRequest
    )
}