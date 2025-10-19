package com.example.scrollbooker.entity.social.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetUserVideoReviewsPostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserVideoReviewsPosts(userId)
    }
}