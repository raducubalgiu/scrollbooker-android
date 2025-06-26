package com.example.scrollbooker.entity.post.domain.useCase

import com.example.scrollbooker.entity.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class UnBookmarkPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
        return try {
            repository.unBookmarkPost(postId)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("UnBookmark Post").e("ERROR: on UnBookmarking post: $e")
            Result.failure(e)
        }
    }
}