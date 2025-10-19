package com.example.scrollbooker.ui.shared.reviews
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserVideoReviewsPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ifEmpty
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewsSummaryUseCase: GetReviewsSummaryUseCase,
    private val getUserVideoReviewsPostsUseCase: GetUserVideoReviewsPostsUseCase
): ViewModel() {
    enum class ReviewsTab { WRITTEN, VIDEO }

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    private val _selectedRatings = MutableStateFlow<Set<Int>>(emptySet())
    val selectedRatings: StateFlow<Set<Int>> = _selectedRatings.asStateFlow()

    private val _currentTab = MutableStateFlow(ReviewsTab.WRITTEN)

    private val _appliedRatingsByTab =
        MutableStateFlow(mapOf(
            ReviewsTab.WRITTEN to emptySet<Int>(),
            ReviewsTab.VIDEO to emptySet<Int>()
        ))

    fun setUserId(id: Int) { if (_userId.value != id) _userId.value = id }

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

    private val _userReviewsSummary =
        MutableStateFlow<FeatureState<ReviewsSummary>>(FeatureState.Loading)
    val userReviewsSummary: StateFlow<FeatureState<ReviewsSummary>> = _userReviewsSummary

    val summaryIsLoading: StateFlow<Boolean> =
        userReviewsSummary.map { it is FeatureState.Loading }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    init {
        viewModelScope.launch {
            userId.filterNotNull()
                .distinctUntilChanged()
                .onEach { _userReviewsSummary.value = FeatureState.Loading }
                .mapLatest { id -> getReviewsSummaryUseCase(id) }
                .catch { _userReviewsSummary.value = FeatureState.Error() }
                .collect { _userReviewsSummary.value = it }
        }
    }

    val writeReviews: Flow<PagingData<Review>> =
        combine(
            userId.filterNotNull(),
            _appliedRatingsByTab.map { it[ReviewsTab.WRITTEN] ?: emptySet<Int>() }
                .distinctUntilChanged()
        ) { uid, ratingsSet -> uid to ratingsSet }
            .flatMapLatest { (uid, ratingsSet) ->
                getReviewsUseCase(
                    userId = uid,
                    ratings = ratingsSet.ifEmpty { null }
                )
            }
            .cachedIn(viewModelScope)

    val videoReviews: Flow<PagingData<Post>> =
        combine(
            userId.filterNotNull(),
            _appliedRatingsByTab.map { it[ReviewsTab.VIDEO] ?: emptySet<Int>() }
                .distinctUntilChanged()
        ) { uid, ratingsSet -> uid to ratingsSet }
            .flatMapLatest { (uid, ratingsSet) ->
                getUserVideoReviewsPostsUseCase(
                    userId = uid,
                    //ratings = ratings.ifEmpty { null }
                )
            }
            .cachedIn(viewModelScope)
}