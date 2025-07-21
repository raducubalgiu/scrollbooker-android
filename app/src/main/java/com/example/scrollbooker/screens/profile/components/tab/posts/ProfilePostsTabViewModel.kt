package com.example.scrollbooker.screens.profile.components.tab.posts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfilePostsTabViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase
): ViewModel() {
    private val userIdState = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun setUserId(userId: Int) {
        if(userIdState.value != userId) {
            userIdState.value = userId
        }
    }
}