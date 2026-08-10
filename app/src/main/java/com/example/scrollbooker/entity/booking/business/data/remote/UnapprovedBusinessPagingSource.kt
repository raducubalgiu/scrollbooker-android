package com.example.scrollbooker.entity.booking.business.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness
import timber.log.Timber
import java.lang.Exception

class UnapprovedBusinessPagingSource(
    private val api: BusinessApiService
) : PagingSource<Int, UnapprovedBusiness>() {

    override fun getRefreshKey(state: PagingState<Int, UnapprovedBusiness>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnapprovedBusiness> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = withVisibleLoading {
                api.getUnapprovedBusinesses(page, limit)
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
            Timber.tag("Unapproved Businesses").e(e, "ERROR: on Loading Unapproved Businesses")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}