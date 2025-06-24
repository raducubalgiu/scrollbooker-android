package com.example.scrollbooker.shared.comment.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsApiService {
    @GET("posts/{postId}/comments")
    suspend fun getCommentsByPostId(
        @Path("postId") postId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<CommentDto>
}