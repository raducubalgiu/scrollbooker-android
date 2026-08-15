package com.example.scrollbooker.entity.social.post.domain.useCase

import com.example.scrollbooker.entity.social.post.data.remote.PostsViewEventsBulkRequest
import com.example.scrollbooker.entity.social.post.data.remote.PostsViewEventsBulkResponse
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class CreatePostsViewEventsBulkUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(request: PostsViewEventsBulkRequest): Result<PostsViewEventsBulkResponse> {
        return try {
            val response = repository.createPostViewEventsBulk(request)
            Result.success(response)
        } catch (e: Exception) {
            Timber.tag("Posts View Events Bulk").e(e, "ERROR: on creating posts view events bulk")
            Result.failure(e)
        }
    }
}