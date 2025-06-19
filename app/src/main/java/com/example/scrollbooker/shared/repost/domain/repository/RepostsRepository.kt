package com.example.scrollbooker.shared.repost.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RepostsRepository {
    fun getUserRepostsPosts(): Flow<PagingData<Post>>
}