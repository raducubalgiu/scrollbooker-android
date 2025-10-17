package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int, text: String, parentId: Int?): Result<Comment> = runCatching {
        repository.createComment(postId, text, parentId)
    }
}