package com.example.scrollbooker.feature.bookmarks.posts.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.feature.posts.data.mappers.toDomain
import com.example.scrollbooker.feature.posts.domain.model.Post
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class BookmarkPostsPagingSource(
    private val api: BookmarkPostsApiService,
) : PagingSource<Int, Post>() {

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            delay(300)

            val response = api.getUserBookmarkedPosts(page, limit)
            val posts = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = posts,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Bookmarked Posts").e("ERROR: on Loading User Bookmarked Posts $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}