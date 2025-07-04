package com.example.scrollbooker.entity.post.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getBookNowPosts(): Flow<PagingData<Post>>
    fun getFollowingPosts(): Flow<PagingData<Post>>
    fun getUserPosts(userId: Int): Flow<PagingData<Post>>
    suspend fun likePost(postId: Int)
    suspend fun unLikePost(postId: Int)
    suspend fun bookmarkPost(postId: Int)
    suspend fun unBookmarkPost(postId: Int)
}