package com.example.scrollbooker.entity.user.notification.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> {
        return repository.getNotifications()
    }
}