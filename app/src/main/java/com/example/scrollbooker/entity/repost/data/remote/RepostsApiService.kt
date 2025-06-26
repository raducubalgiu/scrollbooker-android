package com.example.scrollbooker.entity.repost.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.post.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RepostsApiService {
    @GET("reposts")
    suspend fun getUserRepostsPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}