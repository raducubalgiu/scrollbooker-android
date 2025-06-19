package com.example.scrollbooker.shared.repost.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.shared.repost.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow

class GetUserRepostsUseCase(
    private val repository: RepostsRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getUserRepostsPosts()
    }
}