package com.example.scrollbooker.entity.social.repost.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.repost.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow

class GetUserRepostsUseCase(
    private val repository: RepostsRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Post>> {
        return repository.getUserRepostsPosts(userId)
    }
}