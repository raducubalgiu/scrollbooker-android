package com.example.scrollbooker.entity.social.repost.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.social.post.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepostsApiService {
    @GET("users/{userId}/reposts")
    suspend fun getUserRepostsPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}