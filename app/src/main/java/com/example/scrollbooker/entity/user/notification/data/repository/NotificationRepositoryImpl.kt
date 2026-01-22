package com.example.scrollbooker.entity.user.notification.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationPagingSource
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationsApiService
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationsApiService
): NotificationRepository {
    override suspend fun getUserNotificationsNumber(): Int {
        return api.getUserNotificationsNumber()
    }

    override fun getNotifications(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { NotificationPagingSource(api) }
        ).flow
    }
}