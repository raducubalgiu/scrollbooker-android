package com.example.scrollbooker.entity.social.post.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.social.post.data.mappers.toDomain
import com.example.scrollbooker.entity.social.post.domain.model.Post
import timber.log.Timber
import java.lang.Exception

class PostFollowingPagingSource(
    private val api: PostApiService
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
            val response = api.getFollowingPosts(page, limit)
            val posts = response.results.map { it.toDomain() }

            val totalLoaded = (page - 1) * limit + response.results.size
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if(isLastPage) null else page + 1,
            )
        } catch (e: Exception) {
            Timber.tag("Paging Posts").e("ERROR: on Loading Posts $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}