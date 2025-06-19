package com.example.scrollbooker.shared.bookmark.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.bookmark.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.shared.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

class GetUserBookmarkedPostsUseCase(
    private val repository: BookmarkPostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getUserBookmarkedPosts()
    }
}