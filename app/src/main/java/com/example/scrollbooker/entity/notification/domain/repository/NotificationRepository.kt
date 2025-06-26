package com.example.scrollbooker.entity.notification.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<PagingData<Notification>>
}