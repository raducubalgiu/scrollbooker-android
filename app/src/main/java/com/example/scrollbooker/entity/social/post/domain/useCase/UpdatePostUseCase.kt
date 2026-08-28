package com.example.scrollbooker.entity.social.post.domain.useCase

import com.example.scrollbooker.entity.social.post.data.remote.UpdatePostRequest
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        postId: Int,
        description: String?,
        linkedProductIds: List<Int>,
        customCover: String?
    ): Result<Post> {
        val request = UpdatePostRequest(
            description = description,
            linkedProductIds = linkedProductIds,
            customCover = customCover
        )

        return try {
            val response = repository.updatePostById(postId, request)
            Result.success(response)

        } catch (e: Exception) {
            Timber.tag("Update Post").e(e, "ERROR: on Updating post")
            Result.failure(e)
        }
    }
}