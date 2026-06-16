package com.example.scrollbooker.ui.profile

import androidx.paging.PagingData
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProfilePostDetailViewModelContract {
    val posts: Flow<PagingData<Post>>
    val bookmarks: Flow<PagingData<Post>>

    fun setDetailScreenActive(isActive: Boolean, scopeKey: String, initialIndex: Int, getPost: (Int) -> Post?)
    fun onDetailSessionFinished(scopeKey: String)
    fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?)
    fun getPlayerForIndex(scopeKey: String, index: Int): androidx.media3.common.Player?
    fun observePostUi(postId: Int): StateFlow<PostActionUiState>
    fun togglePlayPause(scopeKey: String, index: Int)
    fun toggleLike(post: Post)
    fun toggleBookmark(post: Post)
}