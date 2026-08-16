package com.example.scrollbooker.components.customized.post.sheets.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import com.example.scrollbooker.entity.social.post.domain.useCase.GetPostAnalyticsSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostStatisticsViewModel @Inject constructor(
    private val getPostAnalyticsSummaryUseCase: GetPostAnalyticsSummaryUseCase
): ViewModel() {
    private val _postId: MutableStateFlow<Int?> = MutableStateFlow<Int?>(null)
    val postId: StateFlow<Int?> = _postId.asStateFlow()

    fun setPostId(postId: Int) {
        _postId.value = postId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val analyticsState: StateFlow<FeatureState<PostAnalyticsSummary>> = _postId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading { getPostAnalyticsSummaryUseCase(id) }

                result
                    .onSuccess { products ->
                        emit(FeatureState.Success(products))
                    }
                    .onFailure { error ->
                        Timber.tag("Post Analytics Summary").e(error, "ERROR: on Fetching Post Analytics Summary")
                        emit(FeatureState.Error(error))
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )
}