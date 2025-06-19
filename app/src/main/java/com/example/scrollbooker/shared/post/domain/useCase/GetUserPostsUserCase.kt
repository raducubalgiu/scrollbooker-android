package com.example.scrollbooker.shared.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.shared.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetUserPostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserPosts(userId)
    }
}