package com.example.scrollbooker.entity.user.notification.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getUserNotificationsNumber(): Int
    suspend fun markNotificationAsRead(notificationIds: List<Int>)
    fun getNotifications(): Flow<PagingData<Notification>>
}