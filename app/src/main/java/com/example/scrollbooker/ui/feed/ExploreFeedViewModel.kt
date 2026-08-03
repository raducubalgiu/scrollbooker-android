package com.example.scrollbooker.ui.feed
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExploreFeedViewModel @Inject constructor(
    getExplorePostsUseCase: GetExplorePostsUseCase,
    postInteractionStore: PostInteractionStore,
    videoPlayerManager: VideoPlayerManager
) : BaseFeedViewModel(
    postInteractionStore,
    videoPlayerManager
) {
    override val feedScopeKey: String = "FEED_EXPLORE"

    override val posts: Flow<PagingData<Post>> =
        getExplorePostsUseCase(selectedBusinessTypes = emptyList())
            .cachedIn(viewModelScope)
}
