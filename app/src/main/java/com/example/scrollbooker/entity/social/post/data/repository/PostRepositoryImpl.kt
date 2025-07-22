package com.example.scrollbooker.entity.social.post.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.data.remote.PostApiService
import com.example.scrollbooker.entity.social.post.data.remote.PostBookNowPagingSource
import com.example.scrollbooker.entity.social.post.data.remote.PostFollowingPagingSource
import com.example.scrollbooker.entity.social.post.data.remote.PostPagingSource
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService
): PostRepository {
    override fun getBookNowPosts(selectedBusinessTypes: List<Int?>): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PostBookNowPagingSource(apiService, selectedBusinessTypes) }
        ).flow
    }

    override fun getFollowingPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PostFollowingPagingSource(apiService) }
        ).flow
    }

    override fun getUserPosts(userId: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PostPagingSource(apiService, userId) }
        ).flow
    }

    override suspend fun likePost(postId: Int) {
        return apiService.likePost(postId)
    }

    override suspend fun unLikePost(postId: Int) {
        return apiService.unLikePost(postId)
    }

    override suspend fun bookmarkPost(postId: Int) {
        return apiService.bookmarkPost(postId)
    }

    override suspend fun unBookmarkPost(postId: Int) {
        return apiService.unBookmarkPost(postId)
    }
}