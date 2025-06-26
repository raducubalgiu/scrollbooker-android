package com.example.scrollbooker.entity.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.entity.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetBookNowPostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getBookNowPosts()
    }
}