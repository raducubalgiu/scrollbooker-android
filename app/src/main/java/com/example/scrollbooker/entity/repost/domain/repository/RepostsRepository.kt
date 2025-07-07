package com.example.scrollbooker.entity.repost.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RepostsRepository {
    fun getUserRepostsPosts(userId: Int): Flow<PagingData<Post>>
}