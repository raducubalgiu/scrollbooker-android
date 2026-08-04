package com.example.scrollbooker.entity.social.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetExplorePostsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(
        serviceIds: List<Int?>,
        onlyVideoReviews: Boolean = false
    ): Flow<PagingData<Post>> {
        return repository.getExplorePosts(serviceIds, onlyVideoReviews)
    }
}