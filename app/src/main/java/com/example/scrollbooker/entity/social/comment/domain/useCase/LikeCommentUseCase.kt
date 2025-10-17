package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import timber.log.Timber
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(commentId: Int): Result<Unit> = runCatching {
        repository.likeComment(commentId)
    }
}