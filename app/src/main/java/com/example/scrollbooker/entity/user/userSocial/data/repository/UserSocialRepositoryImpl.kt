package com.example.scrollbooker.entity.user.userSocial.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialApiService
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialPagingSource
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocialEnum
import com.example.scrollbooker.entity.user.userSocial.domain.repository.UserSocialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSocialRepositoryImpl @Inject constructor(
    private val api: UserSocialApiService
): UserSocialRepository {
    override fun getUserFollowers(userId: Int): Flow<PagingData<UserSocial>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { UserSocialPagingSource(userId, type= UserSocialEnum.FOLLOWERS, api) }
        ).flow
    }

    override fun getUserFollowings(userId: Int): Flow<PagingData<UserSocial>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { UserSocialPagingSource(userId, type = UserSocialEnum.FOLLOWINGS, api) }
        ).flow
    }

    override suspend fun followUser(followeeId: Int) {
        api.followUser(followeeId)
    }

    override suspend fun unfollowUser(followeeId: Int) {
        api.unfollowUser(followeeId)
    }
}