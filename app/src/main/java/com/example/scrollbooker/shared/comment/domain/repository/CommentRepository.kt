package com.example.scrollbooker.shared.comment.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.comment.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>>
}