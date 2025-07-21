package com.example.scrollbooker.entity.social.bookmark.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.bookmark.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

class GetUserBookmarkedPostsUseCase(
    private val repository: BookmarkPostRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserBookmarkedPosts(userId)
    }
}