package com.example.scrollbooker.entity.social.comment.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.model.CommentRepliesPage
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>>
    suspend fun getCommentReplies(postId: Int, parentId: Int, page: Int, limit: Int): CommentRepliesPage
    suspend fun createComment(postId: Int, text: String, parentId: Int?, replyToCommentId: Int?): Comment
    suspend fun likeComment(commentId: Int)
    suspend fun unLikeComment(commentId: Int)
}