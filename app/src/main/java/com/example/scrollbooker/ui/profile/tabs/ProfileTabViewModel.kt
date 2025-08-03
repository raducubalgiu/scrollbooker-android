package com.example.scrollbooker.ui.profile.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileTabViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserRepostsUseCase: GetUserRepostsUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val getServicesByBusinessIdUseCase: GetServicesByBusinessIdUseCase,
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

    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    fun loadServices(businessId: Int?) {
        viewModelScope.launch {
            _servicesState.value = FeatureState.Loading

            if(businessId == null) {
                throw IllegalStateException("Business Id not found")
            }

            val result = withVisibleLoading { getServicesByBusinessIdUseCase(businessId) }

            result
                .onSuccess { response ->
                    _servicesState.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Services").e("ERROR: on Fetching Services in MyProducts $e")
                    _servicesState.value = FeatureState.Error()
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadProducts(serviceId: Int, userId: Int): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            getProductsByUserIdAndServiceIdUseCase(userId, serviceId)
                .cachedIn(viewModelScope)
                .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }
}