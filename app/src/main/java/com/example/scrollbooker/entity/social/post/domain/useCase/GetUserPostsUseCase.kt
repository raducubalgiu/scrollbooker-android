package com.example.scrollbooker.entity.social.post.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetUserPostsUseCase(
    private val repository: PostRepository
) {
    // Unlike every other post-fetching endpoint, the backend does not filter this one
    // by media status/readyToStream - it returns all of a user's posts so the owner's
    // own profile can render still-processing uploads (see PostGrid's placeholder).
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserPosts(userId)
    }
}