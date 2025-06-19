package com.example.scrollbooker.shared.posts.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getUserPosts(userId: Int): Flow<PagingData<Post>>
}