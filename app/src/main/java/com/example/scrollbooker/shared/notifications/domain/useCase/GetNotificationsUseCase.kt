package com.example.scrollbooker.shared.notifications.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.notifications.domain.model.Notification
import com.example.scrollbooker.shared.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> {
        return repository.getNotifications()
    }
}