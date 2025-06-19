package com.example.scrollbooker.shared.bookmark.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkPostRepository {
    fun getUserBookmarkedPosts(): Flow<PagingData<Post>>
}