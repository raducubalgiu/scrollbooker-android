package com.example.scrollbooker.feature.notifications.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.notifications.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<PagingData<Notification>>
}