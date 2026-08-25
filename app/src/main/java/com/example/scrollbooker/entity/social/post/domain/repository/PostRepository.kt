package com.example.scrollbooker.entity.social.post.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.entity.social.post.data.remote.CreatePostRequest
import com.example.scrollbooker.entity.social.post.data.remote.CreateVideoReviewRequest
import com.example.scrollbooker.entity.social.post.data.remote.PostViewEventRequest
import com.example.scrollbooker.entity.social.post.data.remote.PostsViewEventsBulkRequest
import com.example.scrollbooker.entity.social.post.data.remote.PostsViewEventsBulkResponse
import com.example.scrollbooker.entity.social.post.data.remote.UpdatePostRequest
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getExplorePosts(
        serviceIds: List<Int?>,
        onlyVideoReviews: Boolean
    ): Flow<PagingData<Post>>

    fun getFollowingPosts(): Flow<PagingData<Post>>
    fun getUserPosts(userId: Int): Flow<PagingData<Post>>
    fun getUserVideoReviewsPosts(businessId: Int, employeeId: Int?, ratings: Set<Int>?): Flow<PagingData<Post>>

    suspend fun likePost(postId: Int)
    suspend fun unLikePost(postId: Int)
    suspend fun bookmarkPost(postId: Int)
    suspend fun unBookmarkPost(postId: Int)
    suspend fun sharePost(postId: Int, channel: ShareChannelEnum)

    suspend fun updatePostById(postId: Int, request: UpdatePostRequest): Post
    suspend fun getPostById(postId: Int): Post
    suspend fun createPost(request: CreatePostRequest)
    suspend fun createVideoReview(request: CreateVideoReviewRequest)

    suspend fun getPostAnalyticsSummary(postId: Int): PostAnalyticsSummary
    suspend fun createPostViewEvent(postId: Int, request: PostViewEventRequest)
    suspend fun createPostViewEventsBulk(request: PostsViewEventsBulkRequest): PostsViewEventsBulkResponse
}