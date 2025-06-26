package com.example.scrollbooker.entity.review.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.review.data.mappers.toDomain
import com.example.scrollbooker.entity.review.domain.model.Review
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class ReviewPagingSource(
    private val api: ReviewsApiService,
    private val userId: Int,
    private val ratings: Set<Int>?
) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            delay(500)
            val response = api.getReviews(
                userId,
                page,
                limit,
                ratings = ratings?.toList()
            )
            val reviews = response.results.map { it.toDomain() }

            val totalLoaded = (page - 1) * limit + response.results.size
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = reviews,
                prevKey = null,
                nextKey = if(isLastPage) null else page + 1,
            )
        } catch (e: Exception) {
            Timber.tag("Paging Reviews").e("ERROR: on Loading Reviews $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}