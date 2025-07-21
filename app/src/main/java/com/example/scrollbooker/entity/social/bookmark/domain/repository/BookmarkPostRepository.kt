package com.example.scrollbooker.entity.social.bookmark.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkPostRepository {
    fun getUserBookmarkedPosts(userId: Int): Flow<PagingData<Post>>
}