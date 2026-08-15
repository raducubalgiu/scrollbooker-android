package com.example.scrollbooker.ui.feed
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.PostViewHeartbeatTracker
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreFeedViewModel @Inject constructor(
    getExplorePostsUseCase: GetExplorePostsUseCase,
    postInteractionStore: PostInteractionStore,
    videoPlayerManager: VideoPlayerManager,
    postViewHeartbeatTracker: PostViewHeartbeatTracker
) : BaseFeedViewModel(
    postInteractionStore,
    videoPlayerManager,
    postViewHeartbeatTracker
) {
    override val feedScopeKey: String = PostViewSourceEnum.EXPLORE_FEED.key

    private val _selectedServiceIds: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    val selectedServiceIds: Flow<Set<Int>> = _selectedServiceIds.asStateFlow()

    private val _onlyVideoReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onlyVideoReviews: Flow<Boolean> = _onlyVideoReviews.asStateFlow()

    override val posts: Flow<PagingData<Post>> =
        getExplorePostsUseCase(
            serviceIds = _selectedServiceIds.value.toList(),
            onlyVideoReviews = _onlyVideoReviews.value
        )
            .cachedIn(viewModelScope)
}
