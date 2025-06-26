package com.example.scrollbooker.entity.post.domain.useCase

import com.example.scrollbooker.entity.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class BookmarkPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
        return try {
            repository.bookmarkPost(postId)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Bookmark Post").e("ERROR: on Bookmarking post: $e")
            Result.failure(e)
        }
    }
}