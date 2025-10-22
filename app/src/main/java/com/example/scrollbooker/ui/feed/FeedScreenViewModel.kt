package com.example.scrollbooker.ui.feed
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsWithBusinessTypesUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrentPostUi(
    val id: Int,
    val userId: Int,
    val ctaTitle: String,
    val action: PostOverlayActionEnum
)

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase,
    private val getAllBusinessDomainsWithBusinessTypesUseCase: GetAllBusinessDomainsWithBusinessTypesUseCase,
    private val getExplorePostsUseCase: GetExplorePostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    private val application: Application
) : ViewModel() {
    private val _businessDomainsWithBusinessTypes = MutableStateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>>(FeatureState.Loading)
    val businessDomainsWithBusinessTypes: StateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>> = _businessDomainsWithBusinessTypes.asStateFlow()

    private val _currentByTab = MutableStateFlow<Map<Int, CurrentPostUi?>>(emptyMap())
    val currentByTab: StateFlow<Map<Int, CurrentPostUi?>> = _currentByTab.asStateFlow()

    fun updateCurrentPost(tab: Int, post: Post?) {
        val ui = post?.let {
            CurrentPostUi(
                id = it.id,
                userId = it.id,
                ctaTitle = when {
                    it.isVideoReview -> application.getString(R.string.seeMore)
                    it.product != null -> application.getString(R.string.bookThisService)
                    else -> application.getString(R.string.bookNow)
                },
                action = when {
                    it.isVideoReview -> PostOverlayActionEnum.OPEN_REVIEW_DETAILS
                    it.product != null -> PostOverlayActionEnum.OPEN_PRODUCTS
                    else -> PostOverlayActionEnum.OPEN_PRODUCTS
                }
            )
        }
        _currentByTab.update { it + (tab to ui) }
    }

    fun currentPostFor(tab: Int): StateFlow<CurrentPostUi?> =
        currentByTab
            .map { it[tab] }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    private val _filteredBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val filteredBusinessTypes: StateFlow<Set<Int>> = _filteredBusinessTypes

    // Explore Posts
    @OptIn(ExperimentalCoroutinesApi::class)
    val explorePosts: Flow<PagingData<Post>> = filteredBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes -> getExplorePostsUseCase(selectedTypes) }
        .cachedIn(viewModelScope)

    // Following Posts
    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    private val _postUi = MutableStateFlow<Map<Int, PostActionUiState>>(emptyMap())
    fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        _postUi.map { it[postId] ?: PostActionUiState.EMPTY }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PostActionUiState.EMPTY)

    private inline fun MutableStateFlow<Map<Int, PostActionUiState>>.edit(
        postId: Int,
        crossinline reducer: (PostActionUiState) -> PostActionUiState
    ) {
        update { map ->
            val curr = map[postId] ?: PostActionUiState.EMPTY
            map + (postId to reducer(curr))
        }
    }

    private inline fun toggleAction(
        postId: Int,
        backendFlag: Boolean,
        backendCount: Int,
        crossinline isFlagOverridden: (PostActionUiState) -> Boolean?,
        crossinline setFlag: (PostActionUiState, Boolean) -> PostActionUiState,
        crossinline savingOn: (PostActionUiState) -> PostActionUiState,
        crossinline savingOff: (PostActionUiState) -> PostActionUiState,
        crossinline getDelta: (PostActionUiState) -> Int,
        crossinline setDelta: (PostActionUiState, Int) -> PostActionUiState,
        crossinline doOn: suspend () -> Result<Unit>,
        crossinline doOff: suspend () -> Result<Unit>,
    ) {
        val currentPostAction = _postUi.value[postId] ?: PostActionUiState.EMPTY
        val currentFlag = isFlagOverridden(currentPostAction) ?: backendFlag
        val wantOn = !currentFlag

        _postUi.edit(postId) { s ->
            val base = backendCount
            val curr = base + getDelta(s)
            val next = if(wantOn) curr + 1 else curr - 1
            savingOn(setFlag(setDelta(s, next - base), wantOn))
        }

        viewModelScope.launch {
            val result = if(wantOn) doOn() else doOff()
            if(result.isSuccess) {
                _postUi.edit(postId) { s -> savingOff(s) }
            } else {
                _postUi.edit(postId) { s ->
                    val revertedFlag = !(isFlagOverridden(s)!!)
                    val reverted = setFlag(setDelta(s, 0), revertedFlag)
                    savingOff(reverted)
                }
            }
        }
    }

    fun toggleLike(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isLiked,
            backendCount = post.counters.likeCount,
            isFlagOverridden = { it.isLiked },
            setFlag = { s, v -> s.copy(isLiked = v) },
            savingOn = { it.copy(isSavingLike = true) },
            savingOff = { it.copy(isSavingLike = false) },
            getDelta = { it.likeCountDelta },
            setDelta = { s, d -> s.copy(likeCountDelta = d) },
            doOn = { likePostUseCase(post.id) },
            doOff = { unLikePostUseCase(post.id) }
        )
    }

    fun toggleBookmark(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isBookmarked,
            backendCount = post.counters.bookmarkCount,
            isFlagOverridden = { it.isBookmarked },
            setFlag = { s, v -> s.copy(isBookmarked = v) },
            savingOn = { it.copy(isSavingBookmark = true) },
            savingOff = { it.copy(isSavingBookmark = false) },
            getDelta = { it.bookmarkCountDelta },
            setDelta = { s, d -> s.copy(bookmarkCountDelta = d) },
            doOn = { bookmarkPostUseCase(post.id) },
            doOff = { unBookmarkPostUseCase(post.id) }
        )
    }

    fun clearUiState() { _postUi.value = emptyMap() }

    fun updateBusinessTypes() {
        _filteredBusinessTypes.value = _selectedBusinessTypes.value
    }

    fun setBusinessType(id: Int) {
        _selectedBusinessTypes.update { current ->
            if(current.contains(id)) current - id else current + id
        }
    }

    fun clearBusinessTypes() {
        _selectedBusinessTypes.value = emptySet()
    }

    private fun loadAllBusinessDomainsWithBusinessTypes() {
        viewModelScope.launch {
            _businessDomainsWithBusinessTypes.value = FeatureState.Loading
            _businessDomainsWithBusinessTypes.value = getAllBusinessDomainsWithBusinessTypesUseCase()
        }
    }

    init {
        viewModelScope.launch {
            loadAllBusinessDomainsWithBusinessTypes()
        }
    }
}