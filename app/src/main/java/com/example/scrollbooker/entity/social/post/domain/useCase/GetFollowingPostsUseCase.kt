package com.example.scrollbooker.entity.social.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetFollowingPostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getFollowingPosts()
    }
}