package com.example.scrollbooker.ui.feed

import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.flow.StateFlow

abstract class BaseFeedViewModel(
    private val postInteractionStore: PostInteractionStore,
    protected val videoPlayerManager: VideoPlayerManager
) : ViewModel(), FeedViewModelContract {
    abstract val feedScopeKey: String

    override val userPausedPostIds: StateFlow<Set<Int>> = videoPlayerManager.userPausedPostIds

    override fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        postInteractionStore.observePostUi(postId)

    override fun toggleLike(post: Post) {
        postInteractionStore.toggleLike(post)
    }

    override fun toggleBookmark(post: Post) {
        postInteractionStore.toggleBookmark(post)
    }

    override fun getPlayerForIndex(index: Int): ExoPlayer? {
        return videoPlayerManager.getPlayerForIndex(feedScopeKey, index)
    }

    override fun sharePost(post: Post, channel: ShareChannelEnum) {
        postInteractionStore.sharePost(post, channel)
    }

    override fun ensureWindow(centerIndex: Int, getPost: (Int) -> Post?) {
        videoPlayerManager.ensureWindow(
            scopeKey = feedScopeKey,
            centerIndex = centerIndex,
            isScreenActive = true,
            getPost = getPost
        )
    }

    override fun onPageSettled(index: Int) {
        videoPlayerManager.onPageSettled(feedScopeKey, index, isScreenActive = true)
    }

    override fun togglePlayer(index: Int) {
        videoPlayerManager.togglePlayer(feedScopeKey, index)
    }

    override fun stopDetailSession() {
        videoPlayerManager.freezeScreenScope(feedScopeKey)
    }

    override fun resumePlayerOnTabEnter(index: Int) {
        videoPlayerManager.activateScreenScope(feedScopeKey)
        videoPlayerManager.onPageSettled(feedScopeKey, index, isScreenActive = true)
    }
}
