package com.example.scrollbooker.entity.repost.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.entity.repost.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow

class GetUserRepostsUseCase(
    private val repository: RepostsRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getUserRepostsPosts()
    }
}