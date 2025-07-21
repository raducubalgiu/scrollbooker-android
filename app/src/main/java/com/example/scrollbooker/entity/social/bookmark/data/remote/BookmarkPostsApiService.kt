package com.example.scrollbooker.entity.social.bookmark.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.social.post.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkPostsApiService {
    @GET("users/{userId}/bookmark-posts")
    suspend fun getUserBookmarkedPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}