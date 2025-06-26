package com.example.scrollbooker.entity.post.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getBookNowPosts(): Flow<PagingData<Post>>
    fun getFollowingPosts(): Flow<PagingData<Post>>
    fun getUserPosts(userId: Int): Flow<PagingData<Post>>
}