package com.example.scrollbooker.entity.social.comment.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.comment.data.remote.CommentDto
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>>
    suspend fun createComment(postId: Int, text: String, parentId: Int?): CommentDto
    suspend fun likeComment(postId: Int, commentId: Int)
    suspend fun unLikeComment(postId: Int, commentId: Int)
}