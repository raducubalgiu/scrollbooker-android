package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import timber.log.Timber
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int): Result<Unit>  {
        return runCatching {
            repository.likeComment(postId, commentId)
        }.onFailure {
            Timber.tag("Like Comment").e("ERROR: on Liking Comment: $it")
        }
    }
}