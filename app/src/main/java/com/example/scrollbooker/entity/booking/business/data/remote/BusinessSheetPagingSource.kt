package com.example.scrollbooker.entity.booking.business.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import timber.log.Timber
import java.lang.Exception

class BusinessSheetPagingSource(
    private val api: BusinessApiService,
    private val request: SearchBusinessRequest,
    private val onTotalCountChanged: (Int) -> Unit
) : PagingSource<Int, BusinessSheet>() {

    override fun getRefreshKey(state: PagingState<Int, BusinessSheet>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BusinessSheet> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = withVisibleLoading(minLoadingMs = 300L) {
                api.getBusinessesSheet(request, page, limit)
            }

            if(page == 1) {
                onTotalCountChanged(response.count)
            }

            val businesses = response.results.map { it.toDomain() }

            val totalLoaded = (page - 1) * limit + response.results.size
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = businesses,
                prevKey = null,
                nextKey = if(isLastPage) null else page + 1,
            )
        } catch (e: Exception) {
            Timber.tag("Businesses").e("ERROR: on Loading Businesses Sheet $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}