package com.example.scrollbooker.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetRecommendedBusinessesUseCase
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesUseCase
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.search.domain.useCase.CreateUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.DeleteUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.GetUserSearchUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetBookNowPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
    private val getAllBusinessTypesUseCase: GetAllBusinessTypesUseCase,
    private val getAllBusinessDomainsUseCase: GetAllBusinessDomainsUseCase,
    private val getAllBusinessTypesByBusinessDomainUseCase: GetAllBusinessTypesByBusinessDomainUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserSearchUseCase: GetUserSearchUseCase,
    private val createUserSearchUseCase: CreateUserSearchUseCase,
    private val deleteUserSearchUseCase: DeleteUserSearchUseCase,
    private val getBookNowPostsUseCase: GetBookNowPostsUseCase,
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase,
    private val authDataStore: AuthDataStore,
): ViewModel() {
    private val _userProfileState = MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    var appointmentsState by mutableIntStateOf(0)
        private set

    private val _userSearch = MutableStateFlow<FeatureState<UserSearch>>(FeatureState.Loading)
    val userSearch: StateFlow<FeatureState<UserSearch>> = _userSearch

    private val _businessTypesState =
        MutableStateFlow<FeatureState<List<BusinessType>>>(FeatureState.Loading)
    val businessTypesState: StateFlow<FeatureState<List<BusinessType>>> = _businessTypesState

    private val _businessTypesByBusinessDomainState =
        MutableStateFlow<Map<Int, FeatureState<List<BusinessType>>>>(emptyMap())
    val businessTypesByBusinessDomainState: StateFlow<Map<Int, FeatureState<List<BusinessType>>>> = _businessTypesByBusinessDomainState

    private val _businessDomainsState =
        MutableStateFlow<FeatureState<List<BusinessDomain>>>(FeatureState.Loading)
    val businessDomainsState: StateFlow<FeatureState<List<BusinessDomain>>> = _businessDomainsState

    private val _filteredBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val filteredBusinessTypes: StateFlow<Set<Int>> = _filteredBusinessTypes

    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookNowPosts: Flow<PagingData<Post>> = filteredBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes ->
            getBookNowPostsUseCase(selectedTypes)
        }
        .cachedIn(viewModelScope)

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

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

    fun increaseAppointmentsNumber() {
        appointmentsState = appointmentsState + 1
    }

    fun decreaseAppointmentsNumber() {
        if(appointmentsState > 0) {
            appointmentsState = appointmentsState - 1
        }
    }

    fun hasBusinessTypesForMain(businessDomainId: Int): Boolean {
        return _businessTypesByBusinessDomainState.value[businessDomainId] != null
    }

    fun fetchBusinessTypesByBusinessDomain(businessDomainId: Int) {
        viewModelScope.launch {
            _businessTypesByBusinessDomainState.update { current ->
                current + (businessDomainId to FeatureState.Loading)
            }

            val result = withVisibleLoading {
                getAllBusinessTypesByBusinessDomainUseCase(businessDomainId)
            }

            _businessTypesByBusinessDomainState.update { current ->
                current + (businessDomainId to result)
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _userProfileState.value = FeatureState.Loading
            val userId = authDataStore.getUserId().firstOrNull()

            val response = getUserProfileUseCase(userId)
            _userProfileState.value = response
        }
    }

    private fun loadUserSearch() {
        viewModelScope.launch {
            _userSearch.value = FeatureState.Loading
            _userSearch.value = getUserSearchUseCase(
                lng = 44.45050f,
                lat = 25.993102f,
                timezone = "Europe/Bucharest"
            )
        }
    }

    fun createSearch(keyword: String) {
        viewModelScope.launch {
            val response = createUserSearchUseCase(keyword)

            response
                .onFailure { e ->
                    Timber.tag("Search").e("ERROR: on Creating User Search $e")
                }
                .onSuccess { newEntry ->
                    val current = _userSearch.value
                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = buildList {
                            add(newEntry)
                            addAll(current.data.recentlySearch)
                        }.take(20)

                        val updatedUserSearch = current.data.copy(
                            recentlySearch = updatedRecentlySearch
                        )

                        _userSearch.value = FeatureState.Success(updatedUserSearch)
                    }
                }
        }
    }

    fun deleteUserSearch(searchId: Int) {
        viewModelScope.launch {
            val response = deleteUserSearchUseCase(searchId)

            response
                .onFailure { e ->
                    Timber.tag("Search").e("ERROR: on Deleting User Search $e")
                }
                .onSuccess {
                    val current = _userSearch.value
                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = current.data.recentlySearch
                            .filterNot { it.id == searchId }

                        val updatedUserSearch = current.data.copy(
                            recentlySearch = updatedRecentlySearch
                        )

                        _userSearch.value = FeatureState.Success(updatedUserSearch)
                    }
                }
        }
    }

    private fun loadAllBusinessTypes() {
        viewModelScope.launch {
            _businessTypesState.value = FeatureState.Loading
            _businessTypesState.value = getAllBusinessTypesUseCase()
        }
    }

    private fun loadAllBusinessDomains() {
        viewModelScope.launch {
            _businessDomainsState.value = FeatureState.Loading
            _businessDomainsState.value = getAllBusinessDomainsUseCase()
        }
    }

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            appointmentsState = getUserAppointmentsNumberUseCase()
        }
    }

    init {
        viewModelScope.launch {
            launch { loadUserProfile() }
            launch { loadAppointmentsNumber() }
            launch { loadAllBusinessTypes() }
            launch { loadAllBusinessDomains() }
            launch { loadUserSearch() }
        }
    }
}