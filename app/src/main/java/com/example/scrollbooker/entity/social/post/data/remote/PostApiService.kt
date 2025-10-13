package com.example.scrollbooker.entity.social.post.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {
    @GET("posts/explore")
    suspend fun getExplorePosts(
        @Query("business_types") selectedBusinessTypes: List<Int?>,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>

    @GET("posts/following")
    suspend fun getFollowingPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>

    @GET("users/{userId}/posts")
    suspend fun getUserPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>

    @POST("posts/{postId}/likes/")
    suspend fun likePost(
        @Path("postId") postId: Int
    )

    @DELETE("posts/{postId}/likes/")
    suspend fun unLikePost(
        @Path("postId") postId: Int
    )

    @POST("posts/{postId}/bookmark-posts/")
    suspend fun bookmarkPost(
        @Path("postId") postId: Int
    )

    @DELETE("posts/{postId}/bookmark-posts/")
    suspend fun unBookmarkPost(
        @Path("postId") postId: Int
    )
}