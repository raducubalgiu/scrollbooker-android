package com.example.scrollbooker.feature.posts.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.feature.posts.domain.model.Post
import com.example.scrollbooker.feature.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetUserPostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserPosts(userId)
    }
}