package com.example.scrollbooker.shared.posts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.posts.data.remote.PostApiService
import com.example.scrollbooker.shared.posts.data.remote.PostPagingSource
import com.example.scrollbooker.shared.posts.domain.model.Post
import com.example.scrollbooker.shared.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService
): PostRepository {
    override fun getUserPosts(userId: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PostPagingSource(apiService, userId) }
        ).flow
    }
}