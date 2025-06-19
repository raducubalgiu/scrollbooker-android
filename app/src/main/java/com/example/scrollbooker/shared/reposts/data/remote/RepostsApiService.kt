package com.example.scrollbooker.shared.reposts.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.shared.posts.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RepostsApiService {
    @GET("reposts")
    suspend fun getUserRepostsPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}