package com.example.scrollbooker.shared.bookmark.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.bookmark.data.remote.BookmarkPostsApiService
import com.example.scrollbooker.shared.bookmark.data.remote.BookmarkPostsPagingSource
import com.example.scrollbooker.shared.bookmark.domain.repository.BookmarkPostRepository
import com.example.scrollbooker.shared.post.domain.model.Post
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