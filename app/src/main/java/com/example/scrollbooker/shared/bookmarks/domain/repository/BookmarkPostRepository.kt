package com.example.scrollbooker.shared.bookmarks.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkPostRepository {
    fun getUserBookmarkedPosts(): Flow<PagingData<Post>>
}