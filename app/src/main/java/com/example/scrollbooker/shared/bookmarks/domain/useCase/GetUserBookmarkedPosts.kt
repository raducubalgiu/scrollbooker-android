package com.example.scrollbooker.shared.bookmarks.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.bookmarks.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.shared.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

class GetUserBookmarkedPostsUseCase(
    private val repository: BookmarkPostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getUserBookmarkedPosts()
    }
}