package com.example.scrollbooker.ui.feed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUIState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isFirstFrameRendered: Boolean = false,
    val hasStartedPlayback: Boolean = false
)

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase,
    private val getAllBusinessDomainsUseCase: GetAllBusinessDomainsUseCase,
    private val getAllBusinessTypesByBusinessDomainUseCase: GetAllBusinessTypesByBusinessDomainUseCase,
    private val getExplorePostsUseCase: GetExplorePostsUseCase,
) : ViewModel() {
    private val _businessDomainsState =
        MutableStateFlow<FeatureState<List<BusinessDomain>>>(FeatureState.Loading)
    val businessDomainsState: StateFlow<FeatureState<List<BusinessDomain>>> = _businessDomainsState

    private val _businessTypesByBusinessDomainState =
        MutableStateFlow<Map<Int, FeatureState<List<BusinessType>>>>(emptyMap())
    val businessTypesByBusinessDomainState: StateFlow<Map<Int, FeatureState<List<BusinessType>>>> = _businessTypesByBusinessDomainState

    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    private val _filteredBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val filteredBusinessTypes: StateFlow<Set<Int>> = _filteredBusinessTypes

    // Explore Posts
    @OptIn(ExperimentalCoroutinesApi::class)
    val explorePosts: Flow<PagingData<Post>> = filteredBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes -> getExplorePostsUseCase(selectedTypes) }
        .cachedIn(viewModelScope)

    // Following Posts
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

    private fun loadAllBusinessDomains() {
        viewModelScope.launch {
            _businessDomainsState.value = FeatureState.Loading
            _businessDomainsState.value = getAllBusinessDomainsUseCase()
        }
    }

    init {
        viewModelScope.launch {
            loadAllBusinessDomains()
        }
    }
}