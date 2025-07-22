package com.example.scrollbooker.ui.sharedModules.reviews

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ifEmpty

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewsSummaryUseCase: GetReviewsSummaryUseCase
): ViewModel() {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    fun setUserId(userId: Int) {
        if(_userId.value != userId) {
            _userId.value = userId
        }
    }

    private val _userReviewsSummary = MutableStateFlow<FeatureState<ReviewsSummary>>(FeatureState.Loading)
    val userReviewsSummary: StateFlow<FeatureState<ReviewsSummary>> = _userReviewsSummary

    private var _selectedRatings = mutableStateOf(setOf<Int>())
    val selectedRatings: State<Set<Int>> get() = _selectedRatings

    fun toggleRatings(rating: Int) {
        _selectedRatings.value = _selectedRatings.value.toMutableSet().apply {
            if(contains(rating)) remove(rating) else add(rating)
        }
        _filtersTrigger.value = Unit
    }

    private val _filtersTrigger = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userReviews: Flow<PagingData<Review>> =
        combine(
            _filtersTrigger,
            snapshotFlow { _selectedRatings.value },
            userId
        ) { _, ratings, userId ->
            userId to ratings
        }.flatMapLatest { (userId, ratings) ->
            getReviewsUseCase(userId, ratings.ifEmpty { null })
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            userId.collectLatest { currUserId ->
                if(currUserId != null) {
                    _userReviewsSummary.value = FeatureState.Loading
                    _userReviewsSummary.value = getReviewsSummaryUseCase(currUserId)
                } else {
                    Timber.tag("Reviews Summary").e("User Id is null - cannot load reviews summary")
                    _userReviewsSummary.value = FeatureState.Error()
                }
            }
        }
    }
}