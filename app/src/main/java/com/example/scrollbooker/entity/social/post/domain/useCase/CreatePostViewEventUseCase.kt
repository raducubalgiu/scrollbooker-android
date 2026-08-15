package com.example.scrollbooker.entity.social.post.domain.useCase

import com.example.scrollbooker.entity.social.post.data.remote.PostViewEventRequest
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class CreatePostViewEventUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int, request: PostViewEventRequest): Result<Unit> {
        return try {
            repository.createPostViewEvent(postId, request)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Create Post View Event").e(e, "ERROR: on creating post view event")
            Result.failure(e)
        }
    }
}