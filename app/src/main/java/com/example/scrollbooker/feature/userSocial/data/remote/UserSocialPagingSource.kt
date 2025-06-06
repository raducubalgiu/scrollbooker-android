package com.example.scrollbooker.feature.userSocial.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.feature.userSocial.data.mappers.toDomain
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocialEnum
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class UserSocialPagingSource(
    private val userId: Int,
    private val type: UserSocialEnum,
    private val api: UserSocialApiService
) : PagingSource<Int, UserSocial>() {

    override fun getRefreshKey(state: PagingState<Int, UserSocial>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserSocial> {
        val page = params.key ?: 1
        val limit = 20

        return try {
            delay(300)

            val response = when(type) {
                UserSocialEnum.FOLLOWERS -> api.getUserFollowers(userId, page, limit)
                UserSocialEnum.FOLLOWINGS -> api.getUserFollowings(userId, page, limit)
            }

            val userSocials = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = userSocials,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("User Social").e("ERROR: on Fetching User ${type}, $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}