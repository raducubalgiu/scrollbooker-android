package com.example.scrollbooker.shared.notifications.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.notifications.data.remote.NotificationPagingSource
import com.example.scrollbooker.shared.notifications.data.remote.NotificationsApiService
import com.example.scrollbooker.shared.notifications.domain.model.Notification
import com.example.scrollbooker.shared.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationsApiService
): NotificationRepository {
    override fun getNotifications(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { NotificationPagingSource(api) }
        ).flow
    }
}