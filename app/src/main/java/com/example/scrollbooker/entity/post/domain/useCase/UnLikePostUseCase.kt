package com.example.scrollbooker.entity.post.domain.useCase

import com.example.scrollbooker.entity.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class UnLikePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
        return try {
            repository.unLikePost(postId)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Unlike Post").e("ERROR: on UnLiking post: $e")
            Result.failure(e)
        }
    }
}