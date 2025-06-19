package com.example.scrollbooker.shared.reposts.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.posts.domain.model.Post
import com.example.scrollbooker.shared.reposts.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow

class GetUserRepostsUseCase(
    private val repository: RepostsRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getUserRepostsPosts()
    }
}