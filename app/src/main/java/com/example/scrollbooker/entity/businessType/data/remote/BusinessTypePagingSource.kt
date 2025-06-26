package com.example.scrollbooker.entity.businessType.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.businessType.data.mappers.toDomain
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import timber.log.Timber
import java.lang.Exception

class BusinessTypePagingSource(
    private val api: BusinessTypeApiService,
) : PagingSource<Int, BusinessType>() {

    override fun getRefreshKey(state: PagingState<Int, BusinessType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BusinessType> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = api.getAllPaginatedBusinessTypes(page, limit)
            val businessTypes = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = businessTypes,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Business Types").e("ERROR: on Loading Business Types $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}