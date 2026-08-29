package com.example.scrollbooker.ui.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.PostViewHeartbeatTracker
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class FollowingFeedViewModel @Inject constructor(
    getFollowingPostsUseCase: GetFollowingPostsUseCase,
    postInteractionStore: PostInteractionStore,
    videoPlayerManager: VideoPlayerManager,
    postViewHeartbeatTracker: PostViewHeartbeatTracker
) : BaseFeedViewModel(
    postInteractionStore,
    videoPlayerManager,
    postViewHeartbeatTracker
) {
    override val feedScopeKey: String = PostViewSourceEnum.FOLLOWING_FEED.key

    // deletedPostIds is used purely as a refresh trigger here: any change re-runs flatMapLatest,
    // generating a brand new Pager instead of filtering an already-cached PagingData (which
    // crashes with "collect twice from pageEventFlow").
    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<PagingData<Post>> =
        postInteractionStore.deletedPostIds
            .flatMapLatest { getFollowingPostsUseCase() }
            .cachedIn(viewModelScope)
}
