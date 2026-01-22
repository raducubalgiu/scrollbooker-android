package com.example.scrollbooker.entity.user.notification.domain.useCase
import com.example.scrollbooker.entity.user.notification.domain.repository.NotificationRepository

class GetUserNotificationsNumberUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getUserNotificationsNumber()
    }
}