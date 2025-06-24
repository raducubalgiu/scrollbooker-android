package com.example.scrollbooker.shared.comment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.comment.data.remote.CommentsApiService
import com.example.scrollbooker.shared.comment.data.remote.CommentsPagingSource
import com.example.scrollbooker.shared.comment.domain.model.Comment
import com.example.scrollbooker.shared.comment.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val apiService: CommentsApiService,
): CommentRepository {
    override fun getCommentsByPostId(postId: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { CommentsPagingSource(apiService, postId) }
        ).flow
    }

}