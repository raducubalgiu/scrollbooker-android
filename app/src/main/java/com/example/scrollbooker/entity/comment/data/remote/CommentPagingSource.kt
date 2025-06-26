package com.example.scrollbooker.entity.comment.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.comment.data.mappers.toDomain
import com.example.scrollbooker.entity.comment.domain.model.Comment
import timber.log.Timber
import java.lang.Exception

class CommentsPagingSource(
    private val api: CommentsApiService,
    private val postId: Int
) : PagingSource<Int, Comment>() {

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = api.getCommentsByPostId(postId, page, limit)
            val comments = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = comments,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Comments").e("ERROR: on Loading Post Comments $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}