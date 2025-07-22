package com.example.scrollbooker.ui.sharedModules.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByIdUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsPagerViewModel @Inject constructor(
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    private val getBusinessByIdUseCase: GetBusinessByIdUseCase
): ViewModel() {
    private val _interactionMap = mutableMapOf<Int, MutableStateFlow<PostInteractionState>>()

    fun interactionState(postId: Int): StateFlow<PostInteractionState> {
        return _interactionMap.getOrPut(postId) {
            MutableStateFlow(PostInteractionState())
        }
    }

    private val cachedBusinesses = mutableMapOf<Int, Business>()
    private val _businessId = MutableStateFlow<Int?>(null)
    val businessId: StateFlow<Int?> = _businessId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val businessState: StateFlow<FeatureState<Business>> =
        businessId
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { id ->
                flow {
                    emit(FeatureState.Loading)

                    val cached = cachedBusinesses[id]

                    if(cached != null) {
                        emit(FeatureState.Success(cached))
                    } else {
                        val result = withVisibleLoading { getBusinessByIdUseCase(id) }
                        if(result is FeatureState.Success) {
                            cachedBusinesses[id] = result.data
                        }
                        emit(result)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeatureState.Loading
            )

    fun setBusinessId(newBusinessId: Int) {
        if(_businessId.value != newBusinessId) {
            _businessId.value = newBusinessId
        }
    }

    fun setInitialState(
        postId: Int,
        isLiked: Boolean,
        likeCount: Int,
        isBookmarked: Boolean,
        bookmarkCount: Int
    ) {
        val flow = _interactionMap.getOrPut(postId) {
            MutableStateFlow(PostInteractionState())
        }

        if(flow.value == PostInteractionState()) {
            flow.value = PostInteractionState(
                isLiked = isLiked,
                likeCount = likeCount,
                isBookmarked = isBookmarked,
                bookmarkCount = bookmarkCount
            )
        }
    }

    fun toggleLike(postId: Int) {
        val flow = _interactionMap[postId] ?: return
        val current = flow.value

        if(current.isLiking) return

        val newState = current.copy(
            isLiked = !current.isLiked,
            likeCount = current.likeCount + if(!current.isLiked) 1 else -1,
            isLiking = true
        )

        flow.value = newState

        viewModelScope.launch {
            val result = if(!current.isLiked) {
                likePostUseCase(postId)
            } else {
                unLikePostUseCase(postId)
            }

            flow.value = if(result.isFailure) current else newState.copy(isLiking = false)
        }
    }

    fun toggleBookmark(postId: Int) {
        val flow = _interactionMap[postId] ?: return
        val current = flow.value

        if(current.isBookmarking) return

        val newState = current.copy(
            isBookmarked = !current.isBookmarked,
            bookmarkCount = current.bookmarkCount + if(!current.isBookmarked) 1 else -1,
            isBookmarking = true
        )

        flow.value = newState

        viewModelScope.launch {
            val result = if(!current.isBookmarked) {
                bookmarkPostUseCase(postId)
            } else {
                unBookmarkPostUseCase(postId)
            }

            flow.value = if(result.isFailure) current else newState.copy(isBookmarking = false)
        }
    }
}