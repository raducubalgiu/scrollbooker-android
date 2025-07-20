package com.example.scrollbooker.entity.search.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import timber.log.Timber
import java.lang.Exception

class SearchUsersPagingSource(
    private val api: SearchApiService,
    private val query: String
) : PagingSource<Int, UserSocial>() {

    override fun getRefreshKey(state: PagingState<Int, UserSocial>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserSocial> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = if(page == 1) {
                withVisibleLoading { api.searchPaginatedUsers(query, page, limit) }
            } else {
                api.searchPaginatedUsers(query, page, limit)
            }
            val users = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = users,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Search Users").e("ERROR: on Searching Users $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}