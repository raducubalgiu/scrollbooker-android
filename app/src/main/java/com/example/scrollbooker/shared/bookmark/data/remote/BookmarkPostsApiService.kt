package com.example.scrollbooker.shared.bookmark.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.shared.post.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkPostsApiService {
    @GET("bookmark-posts")
    suspend fun getUserBookmarkedPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}