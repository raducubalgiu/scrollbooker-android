package com.example.scrollbooker.shared.post.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {
    @GET("posts/book-now")
    suspend fun getBookNowPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>

    @GET("posts/following")
    suspend fun getFollowingPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>

    @GET("users/{userId}/posts/")
    suspend fun getUserPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}