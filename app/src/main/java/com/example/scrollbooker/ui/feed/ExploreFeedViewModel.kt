package com.example.scrollbooker.ui.feed
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.PostViewHeartbeatTracker
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetAllServiceDomainsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreFeedViewModel @Inject constructor(
    getExplorePostsUseCase: GetExplorePostsUseCase,
    postInteractionStore: PostInteractionStore,
    videoPlayerManager: VideoPlayerManager,
    postViewHeartbeatTracker: PostViewHeartbeatTracker,
    private val getAllServiceDomainsUseCase: GetAllServiceDomainsUseCase
) : BaseFeedViewModel(
    postInteractionStore,
    videoPlayerManager,
    postViewHeartbeatTracker
) {
    override val feedScopeKey: String = PostViewSourceEnum.EXPLORE_FEED.key

    private val _serviceDomains = MutableStateFlow<FeatureState<List<ServiceDomain>>>(FeatureState.Loading)
    val serviceDomains: StateFlow<FeatureState<List<ServiceDomain>>> = _serviceDomains.asStateFlow()

    private val _selectedServiceIds: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    val selectedServiceIds: StateFlow<Set<Int>> = _selectedServiceIds.asStateFlow()

    private val _onlyVideoReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onlyVideoReviews: StateFlow<Boolean> = _onlyVideoReviews.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<PagingData<Post>> = combine(
        _selectedServiceIds,
        _onlyVideoReviews
    ) { ids, onlyVideos ->
        Pair(ids.toList(), onlyVideos)
    }.flatMapLatest { (idsList, onlyVideos) ->
        getExplorePostsUseCase(
            serviceIds = idsList,
            onlyVideoReviews = onlyVideos
        )
    }.cachedIn(viewModelScope)

    fun loadAllServiceDomains() {
        viewModelScope.launch {
            _serviceDomains.value = FeatureState.Loading

            val result = getAllServiceDomainsUseCase()

            result.fold(
                onSuccess = { domains ->
                    _serviceDomains.value = FeatureState.Success(domains)
                },
                onFailure = { error ->
                    _serviceDomains.value = FeatureState.Error(error)
                }
            )
        }
    }

    fun setSelectedServiceIds(newServiceIds: Set<Int>) {
        if (newServiceIds == _selectedServiceIds.value) return

        _selectedServiceIds.value = newServiceIds
        requestScrollToTop()
    }

    fun setOnlyVideoReviews(newOnlyVideoReviews: Boolean) {
        if (newOnlyVideoReviews == _onlyVideoReviews.value) return

        _onlyVideoReviews.value = newOnlyVideoReviews
        requestScrollToTop()
    }

    init {
        loadAllServiceDomains()
    }
}
