package com.example.scrollbooker.feature.notifications.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.feature.notifications.data.mappers.toDomain
import com.example.scrollbooker.feature.notifications.domain.model.Notification
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class NotificationPagingSource(
    private val api: NotificationsApiService
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            delay(300)
            val response = api.getUserNotifications(page, limit)
            val notifications = response.results.map { it.toDomain() }

            val totalLoaded = (page - 1) * limit + response.results.size
            val isLastPage = totalLoaded >= response.count
            Timber.tag("Paging").e("-> page$page, received=${notifications.size}, isLatPage=$isLastPage")

            LoadResult.Page(
                data = notifications,
                prevKey = null,
                nextKey = if(isLastPage) null else page + 1,
            )
        } catch (e: Exception) {
            Timber.tag("Paging Notifications").e("ERROR: on Loading Notifications $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}