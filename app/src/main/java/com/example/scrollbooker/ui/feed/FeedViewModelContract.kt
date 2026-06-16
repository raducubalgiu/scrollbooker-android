package com.example.scrollbooker.ui.feed

import androidx.paging.PagingData
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FeedViewModelContract {
    val posts: Flow<PagingData<Post>>
    val userPausedPostIds: StateFlow<Set<Int>>

    fun observePostUi(postId: Int): StateFlow<PostActionUiState>
    fun toggleLike(post: Post)
    fun toggleBookmark(post: Post)

    fun getPlayerForIndex(index: Int): androidx.media3.common.Player?
    fun ensureWindow(centerIndex: Int, getPost: (Int) -> Post?)
    fun onPageSettled(index: Int)
    fun togglePlayer(index: Int)
    fun stopDetailSession()
    fun resumePlayerOnTabEnter(index: Int)
}
