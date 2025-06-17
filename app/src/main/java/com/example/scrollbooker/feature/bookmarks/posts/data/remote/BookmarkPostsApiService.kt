package com.example.scrollbooker.feature.bookmarks.posts.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.feature.posts.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkPostsApiService {
    @GET("bookmark-posts")
    suspend fun getUserBookmarkedPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<PostDto>
}