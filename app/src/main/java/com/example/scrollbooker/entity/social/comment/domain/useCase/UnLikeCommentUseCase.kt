package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import timber.log.Timber
import javax.inject.Inject

class UnLikeCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int): Result<Unit>  {
        return runCatching {
            repository.unLikeComment(postId, commentId)
        }.onFailure {
            Timber.tag("UnLike Comment").e("ERROR: on UnLiking Comment: $it")
        }
    }
}