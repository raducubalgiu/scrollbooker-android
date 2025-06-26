package com.example.scrollbooker.entity.comment.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.comment.domain.model.Comment
import com.example.scrollbooker.entity.comment.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

class GetPaginatedPostCommentsUseCase(
    private val repository: CommentRepository
) {
    operator fun invoke(postId: Int): Flow<PagingData<Comment>> {
        return repository.getCommentsByPostId(postId)
    }
}