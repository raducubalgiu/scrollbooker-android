package com.example.scrollbooker.feature.notifications.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.feature.notifications.data.mappers.toDomain
import com.example.scrollbooker.feature.notifications.domain.model.Notification
import timber.log.Timber
import java.lang.Exception

class NotificationPagingSource(
    private val api: NotificationsApiService
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: 1
        val limit = params.loadSize

        return try {
            val response = api.getUserNotifications(page, limit)
            val notifications = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = notifications,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Notifications").e("ERROR: on Loading Notifications $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}