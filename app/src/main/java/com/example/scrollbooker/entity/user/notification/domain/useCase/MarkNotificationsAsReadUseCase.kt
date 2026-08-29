package com.example.scrollbooker.entity.user.notification.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.user.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationsAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationsIds: List<Int>): Result<Unit> {
        return runSuspendCatching {
            repository.markNotificationAsRead(notificationsIds)
        }
    }
}