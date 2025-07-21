package com.example.scrollbooker.entity.social.repost.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RepostsRepository {
    fun getUserRepostsPosts(userId: Int): Flow<PagingData<Post>>
}