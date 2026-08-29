package com.example.scrollbooker.ui.reviews
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.PostViewHeartbeatTracker
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.LikeWrittenReviewUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.UnlikeWrittenReviewUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserVideoReviewsPostsUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewsSummaryUseCase: GetReviewsSummaryUseCase,
    private val getUserVideoReviewsPostsUseCase: GetUserVideoReviewsPostsUseCase,
    private val likeReviewUseCase: LikeWrittenReviewUseCase,
    private val unlikeReviewUseCase: UnlikeWrittenReviewUseCase,
    private val authDataStore: AuthDataStore,
    private val postInteractionStore: PostInteractionStore,
    private val videoPlayerManager: VideoPlayerManager,
    @Suppress("UNUSED_PARAMETER") postViewHeartbeatTracker: PostViewHeartbeatTracker
): ViewModel() {
    enum class ReviewsTab(val key: String) {
        ALL("all"),
        VIDEO("video");

        companion object {
            fun fromKey(key: String): ReviewsTab? = entries.find { it.key == key }
        }
    }

    val businessId: Int = savedStateHandle["businessId"] ?: error("Missing businessId")
    val employeeId: Int? = (savedStateHandle.get<Int>("employeeId") ?: -1).takeIf { it != -1 }

    private val _selectedRatings = MutableStateFlow<Set<Int>>(emptySet())
    val selectedRatings: StateFlow<Set<Int>> = _selectedRatings.asStateFlow()

    private val _currentTab = MutableStateFlow(ReviewsTab.ALL)

    private val _appliedRatingsByTab =
        MutableStateFlow(mapOf(
            ReviewsTab.ALL to emptySet<Int>(),
            ReviewsTab.VIDEO to emptySet<Int>()
        ))

    fun clearRatings() {
        if(_selectedRatings.value.isNotEmpty()) {
            _selectedRatings.value = emptySet()
        }
    }

    fun setTab(tab: ReviewsTab) {
        _currentTab.value = tab

        val cur = _selectedRatings.value
        val map = _appliedRatingsByTab.value

        if(map[tab] != cur) {
            _appliedRatingsByTab.value = map.toMutableMap().apply { put(tab, cur) }
        }
    }

    fun toggleRating(rating: Int) {
        val newSet = _selectedRatings.value.toMutableSet().apply {
            if(contains(rating)) remove(rating) else add(rating)
        }
        _selectedRatings.value = newSet

        val tab = _currentTab.value
        _appliedRatingsByTab.value =
            _appliedRatingsByTab.value.toMutableMap().apply { put(tab, newSet) }
    }

    private val _userReviewsSummary = MutableStateFlow<FeatureState<ReviewsSummary>>(FeatureState.Loading)
    val userReviewsSummary: StateFlow<FeatureState<ReviewsSummary>> = _userReviewsSummary

    val summaryIsLoading: StateFlow<Boolean> =
        userReviewsSummary.map { it is FeatureState.Loading }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    init {
        viewModelScope.launch {
            _userReviewsSummary.value = FeatureState.Loading
            _userReviewsSummary.value = getReviewsSummaryUseCase(businessId = businessId, employeeId = employeeId)
        }
    }

    val allReviews: Flow<PagingData<Review>> =
        _appliedRatingsByTab.map { it[ReviewsTab.ALL] ?: emptySet<Int>() }
            .distinctUntilChanged()
            .flatMapLatest { ratingsSet ->
                getReviewsUseCase(
                    businessId = businessId,
                    employeeId = employeeId,
                    ratings = ratingsSet.ifEmpty { null }
                )
            }
            .map { paging: PagingData<Review> ->
                paging.map { r: Review ->
                    seedReviewUiIfAbsent(r)
                    r
                }
            }
            .cachedIn(viewModelScope)

    private val _videoReviewsRefreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // Called after a video review's underlying post gets deleted from ReviewsDetailScreen,
    // once its sheet has finished closing (same "regenerate the Pager" approach used for
    // feed/profile posts - see PostInteractionStore).
    fun refreshAfterPostDeleted() {
        _videoReviewsRefreshTrigger.tryEmit(Unit)
    }

    val videoReviews: Flow<PagingData<Post>> =
        combine(
            _appliedRatingsByTab.map { it[ReviewsTab.VIDEO] ?: emptySet<Int>() }.distinctUntilChanged(),
            _videoReviewsRefreshTrigger.onStart { emit(Unit) }
        ) { ratingsSet, _ -> ratingsSet }
            .flatMapLatest { ratingsSet ->
                getUserVideoReviewsPostsUseCase(
                    businessId = businessId,
                    employeeId = employeeId,
                    ratings = ratingsSet.ifEmpty { null }
                )
            }
            .cachedIn(viewModelScope)

    private val _reviewUi = MutableStateFlow<Map<Int, ReviewActionUiState>>(emptyMap())

    fun observeReviewUi(reviewId: Int): StateFlow<ReviewActionUiState> =
        _reviewUi.map { it[reviewId] ?: ReviewActionUiState.EMPTY }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ReviewActionUiState.EMPTY
            )

    private fun seedReviewUiIfAbsent(review: Review) {
        _reviewUi.update { cur ->
            if (cur.containsKey(review.id)) cur
            else cur + (review.id to ReviewActionUiState(
                likeCount = review.likeCount,
                isLiked = review.isLiked,
                isLikedByProductOwner = review.isLikedByProductOwner
            ))
        }
    }

    fun toggleLike(reviewId: Int, productOwnerId: Int) = viewModelScope.launch {
        val uid = authDataStore.getUserId().firstOrNull() ?: return@launch

        val before = _reviewUi.value[reviewId] ?: ReviewActionUiState.EMPTY
        if (before.isSavingLike) return@launch

        val isOwner = uid == productOwnerId
        val willLike = !before.isLiked

        val optimistic = if (willLike) {
            before.copy(
                isLiked = true,
                likeCount = before.likeCount + 1,
                isLikedByProductOwner = if (isOwner) true else before.isLikedByProductOwner,
                isSavingLike = true
            )
        } else {
            before.copy(
                isLiked = false,
                likeCount = (before.likeCount - 1).coerceAtLeast(0),
                isLikedByProductOwner = if (isOwner) false else before.isLikedByProductOwner,
                isSavingLike = true
            )
        }
        _reviewUi.update { it + (reviewId to optimistic) }

        val result = if (willLike) likeReviewUseCase(reviewId) else unlikeReviewUseCase(reviewId)

        if (result.isSuccess) {
            _reviewUi.update { map ->
                val cur = map[reviewId] ?: return@update map
                map + (reviewId to cur.copy(isSavingLike = false))
            }
        } else {
            _reviewUi.update { it + (reviewId to before.copy(isSavingLike = false)) }
        }
    }

    // ---- Video review detail pager (ReviewsDetailScreen) ----
    // Video reviews are regular Posts, so likes/bookmarks/share and player management reuse the
    // same singletons every other post detail/feed screen uses (PostInteractionStore, VideoPlayerManager),
    // instead of the written-review specific state above.

    val userPausedPostIds: StateFlow<Set<Int>> = videoPlayerManager.userPausedPostIds

    fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        postInteractionStore.observePostUi(postId)

    fun toggleLike(post: Post) {
        postInteractionStore.toggleLike(post)
    }

    fun toggleBookmark(post: Post) {
        postInteractionStore.toggleBookmark(post)
    }

    fun sharePost(post: Post, channel: ShareChannelEnum) {
        postInteractionStore.sharePost(post, channel)
    }

    fun setDetailScreenActive(isActive: Boolean, scopeKey: String, centerIndex: Int, getPost: (Int) -> Post?) {
        if (isActive) {
            videoPlayerManager.activateScreenScope(scopeKey)
        }
        videoPlayerManager.ensureWindow(scopeKey, centerIndex, isActive, getPost)
    }

    fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?) {
        videoPlayerManager.onPageSettled(scopeKey, index, true)
        videoPlayerManager.ensureWindow(scopeKey, index, true, getPost)
    }

    fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? =
        videoPlayerManager.getPlayerForIndex(scopeKey, index)

    fun togglePlayPause(scopeKey: String, index: Int) {
        videoPlayerManager.togglePlayer(scopeKey, index)
    }

    fun onDetailSessionFinished(scopeKey: String) {
        videoPlayerManager.releaseScreenScope(scopeKey)
    }
}
