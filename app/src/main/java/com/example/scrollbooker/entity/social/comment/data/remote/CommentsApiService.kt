package com.example.scrollbooker.entity.social.comment.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsApiService {
    @GET("posts/{postId}/comments")
    suspend fun getCommentsByPostId(
        @Path("postId") postId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<CommentDto>

    @POST("posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Body request: CreateCommentDto
    ): CommentDto

    @POST("comments/{commentId}/likes")
    suspend fun likeComment(
        @Path("commentId") commentId: Int
    )

    @POST("comments/{commentId}/likes")
    suspend fun unlikeComment(
        @Path("commentId") commentId: Int
    )
}