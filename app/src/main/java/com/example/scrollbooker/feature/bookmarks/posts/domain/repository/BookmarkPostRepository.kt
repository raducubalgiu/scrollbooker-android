package com.example.scrollbooker.feature.bookmarks.posts.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkPostRepository {
    fun getUserBookmarkedPosts(): Flow<PagingData<Post>>
}