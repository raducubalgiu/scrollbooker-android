package com.example.scrollbooker.entity.user.userSocial.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import kotlinx.coroutines.flow.Flow

interface UserSocialRepository {
    fun getUserFollowers(userId: Int): Flow<PagingData<UserSocial>>
    fun getUserFollowings(userId: Int): Flow<PagingData<UserSocial>>
    suspend fun followUser(followeeId: Int)
    suspend fun unfollowUser(followeeId: Int)
}