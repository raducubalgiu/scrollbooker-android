package com.example.scrollbooker.entity.social.post.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.social.post.data.mappers.toDomain
import com.example.scrollbooker.entity.social.post.domain.model.Post
import timber.log.Timber
import java.lang.Exception
import kotlin.coroutines.cancellation.CancellationException

class PostVideoReviewsPagingSource(
    private val api: PostApiService,
    private val businessId: Int,
    private val employeeId: Int?,
    private val ratings: Set<Int>?
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
            val call: suspend () -> PaginatedResponseDto<PostDto> = {
                api.getUserVideoReviewsPosts(
                    businessId = businessId,
                    employeeId = employeeId,
                    ratings = ratings?.toList(),
                    page = page,
                    limit = limit
                )
            }

            val response = if (page == 1) withVisibleLoading { call() } else call()
            val posts = response.results.map { it.toDomain() }

            val totalLoaded = (page - 1) * limit + response.results.size
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if(isLastPage) null else page + 1,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.tag("Paging Posts").e(e, "ERROR: on Loading User Video Reviews Posts")
            LoadResult.Error(throwable = e)
        }
    }
}