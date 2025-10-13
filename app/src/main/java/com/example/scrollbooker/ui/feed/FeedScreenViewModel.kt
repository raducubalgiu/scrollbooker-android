package com.example.scrollbooker.ui.feed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsWithBusinessTypesUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase,
    private val getAllBusinessDomainsWithBusinessTypesUseCase: GetAllBusinessDomainsWithBusinessTypesUseCase,
    private val getExplorePostsUseCase: GetExplorePostsUseCase,
) : ViewModel() {
    private val _businessDomainsWithBusinessTypes = MutableStateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>>(FeatureState.Loading)
    val businessDomainsWithBusinessTypes: StateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>> = _businessDomainsWithBusinessTypes.asStateFlow()

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

    private fun loadAllBusinessDomainsWithBusinessTypes() {
        viewModelScope.launch {
            _businessDomainsWithBusinessTypes.value = FeatureState.Loading
            _businessDomainsWithBusinessTypes.value = getAllBusinessDomainsWithBusinessTypesUseCase()
        }
    }

    init {
        viewModelScope.launch {
            loadAllBusinessDomainsWithBusinessTypes()
        }
    }
}