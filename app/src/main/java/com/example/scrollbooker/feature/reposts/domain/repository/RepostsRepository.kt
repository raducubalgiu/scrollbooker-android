package com.example.scrollbooker.feature.reposts.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RepostsRepository {
    fun getUserRepostsPosts(): Flow<PagingData<Post>>
}