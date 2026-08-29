package com.example.scrollbooker.entity.social.comment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.comment.data.mappers.toDomain
import com.example.scrollbooker.entity.social.comment.data.mappers.toDto
import com.example.scrollbooker.entity.social.comment.data.remote.CommentsApiService
import com.example.scrollbooker.entity.social.comment.data.remote.CommentsPagingSource
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.model.CommentRepliesPage
import com.example.scrollbooker.entity.social.comment.domain.model.CreateComment
import com.example.scrollbooker.entity.social.comment.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val apiService: CommentsApiService,
): CommentRepository {
    override fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { CommentsPagingSource(apiService, postId) }
        ).flow
    }

    override suspend fun getCommentReplies(postId: Int, parentId: Int, page: Int, limit: Int): CommentRepliesPage {
        val response = apiService.getCommentReplies(postId, parentId, page, limit)
        val totalLoaded = page * limit

        return CommentRepliesPage(
            items = response.results.map { it.toDomain() },
            hasMore = totalLoaded < response.count
        )
    }

    override suspend fun createComment(postId: Int, text: String, parentId: Int?, replyToCommentId: Int?): Comment {
        val request = CreateComment(text = text, parentId = parentId, replyToCommentId = replyToCommentId).toDto()
        return apiService.createComment(postId, request).toDomain()
    }

    override suspend fun likeComment(commentId: Int) {
        return apiService.likeComment(commentId)
    }

    override suspend fun unLikeComment(commentId: Int) {
        return apiService.unlikeComment(commentId)
    }

}