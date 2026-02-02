package com.example.scrollbooker.ui.profile.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.repost.domain.useCase.GetUserRepostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileLayoutViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserRepostsUseCase: GetUserRepostsUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val getServicesByUserIdUseCase: GetServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase
): ViewModel() {
    private val userIdFlow = MutableStateFlow<Int?>(null)

    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _headerOffset = MutableStateFlow<Float>(0f)
    val headerOffset: StateFlow<Float> = _headerOffset.asStateFlow()

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

    fun setHeaderOffset(offset: Float) {
        _headerOffset.value = offset
    }

    fun setUserId(userId: Int?) {
        if(userIdFlow.value != userId) {
            userIdFlow.value = userId
        }
    }

    // Tabs
    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = userIdFlow
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PagingData.empty()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = userIdFlow
        .filterNotNull()
        .flatMapLatest { userId -> getEmployeesByOwnerUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val reposts: Flow<PagingData<Post>> = userIdFlow
        .filterNotNull()
        .flatMapLatest { userId -> getUserRepostsUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarks: Flow<PagingData<Post>> = userIdFlow
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserBookmarkedPostsUseCase(userId)
        }
        .cachedIn(viewModelScope)

    val about: StateFlow<FeatureState<UserProfileAbout>> =
        flow<FeatureState<UserProfileAbout>> {
            emit(FeatureState.Loading)
            emit(withVisibleLoading { getUserProfileAboutUseCase() })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedules: StateFlow<FeatureState<List<Schedule>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getSchedulesByUserIdUseCase(userId)
                }

                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val servicesState: StateFlow<FeatureState<List<ServiceWithEmployees>>> = userIdFlow
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