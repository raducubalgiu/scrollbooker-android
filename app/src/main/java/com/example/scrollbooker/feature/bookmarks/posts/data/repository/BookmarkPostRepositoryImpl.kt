package com.example.scrollbooker.feature.bookmarks.posts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.feature.bookmarks.posts.data.remote.BookmarkPostsApiService
import com.example.scrollbooker.feature.bookmarks.posts.data.remote.BookmarkPostsPagingSource
import com.example.scrollbooker.feature.bookmarks.posts.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.shared.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkPostRepositoryImpl @Inject constructor(
    private val apiService: BookmarkPostsApiService
): BookmarkPostRepository {
    override fun getUserBookmarkedPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { BookmarkPostsPagingSource(apiService) }
        ).flow
    }
}