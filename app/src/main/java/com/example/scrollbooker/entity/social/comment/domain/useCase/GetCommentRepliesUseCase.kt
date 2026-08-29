package com.example.scrollbooker.entity.social.comment.domain.useCase
import com.example.scrollbooker.entity.social.comment.domain.model.CommentRepliesPage
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentRepliesUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int, parentId: Int, page: Int, limit: Int): Result<CommentRepliesPage> = runCatching {
        repository.getCommentReplies(postId, parentId, page, limit)
    }
}
