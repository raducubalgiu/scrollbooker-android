package com.example.scrollbooker.entity.post.domain.useCase
import com.example.scrollbooker.entity.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class LikePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
       return try {
           repository.likePost(postId)
           Result.success(Unit)

       } catch (e: Exception) {
           Timber.tag("Like Post").e("ERROR: on Liking post: $e")
           Result.failure(e)
       }
    }
}