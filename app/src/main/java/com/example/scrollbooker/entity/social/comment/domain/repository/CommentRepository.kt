package com.example.scrollbooker.entity.social.comment.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>>
    suspend fun createComment(postId: Int, text: String, parentId: Int?): Comment
    suspend fun likeComment(commentId: Int)
    suspend fun unLikeComment(commentId: Int)
}