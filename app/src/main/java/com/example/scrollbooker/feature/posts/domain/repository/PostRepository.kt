package com.example.scrollbooker.feature.posts.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getUserPosts(userId: Int): Flow<PagingData<Post>>
}