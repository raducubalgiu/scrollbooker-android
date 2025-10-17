package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import javax.inject.Inject

class UnLikeCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(commentId: Int): Result<Unit> = runCatching {
        repository.unLikeComment(commentId)
    }
}