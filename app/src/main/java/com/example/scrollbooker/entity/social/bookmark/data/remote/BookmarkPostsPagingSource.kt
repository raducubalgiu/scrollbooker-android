package com.example.scrollbooker.entity.social.bookmark.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.social.post.data.mappers.toDomain
import com.example.scrollbooker.entity.social.post.domain.model.Post
import timber.log.Timber
import java.lang.Exception

class BookmarkPostsPagingSource(
    private val api: BookmarkPostsApiService,
    private val userId: Int
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
            val response = if(page == 1) {
                withVisibleLoading { api.getUserBookmarkedPosts(userId, page, limit) }
            } else {
                api.getUserBookmarkedPosts(userId, page, limit)
            }

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