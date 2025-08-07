package com.example.scrollbooker.ui.profile.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.repost.domain.useCase.GetUserRepostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileTabViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserRepostsUseCase: GetUserRepostsUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val getServicesByUserIdUseCase: GetServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase
): ViewModel() {
    private val userIdState = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @OptIn(ExperimentalCoroutinesApi::class)
    val userReposts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserRepostsUseCase(userId)
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PagingData.empty()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val userBookmarkedPosts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserBookmarkedPostsUseCase(userId)
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun setUserId(userId: Int) {
        if(userIdState.value != userId) {
            userIdState.value = userId
        }
    }

    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val servicesState: StateFlow<FeatureState<List<ServiceWithEmployees>>> = userIdState
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getServicesByUserIdUseCase(userId)
                }

                emit(
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("Services").e("ERROR: on Fetching Services in MyProducts $e")
                            FeatureState.Error()
                        }
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadProducts(serviceId: Int, userId: Int, employeeId: Int?): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            getProductsByUserIdAndServiceIdUseCase(userId, serviceId, employeeId)
                .cachedIn(viewModelScope)
                .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }
}