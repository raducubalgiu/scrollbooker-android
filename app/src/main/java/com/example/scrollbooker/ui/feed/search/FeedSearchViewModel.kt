package com.example.scrollbooker.ui.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.useCase.CreateUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.DeleteUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.GetRecentlySearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class FeedSearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val getRecentlySearchUseCase: GetRecentlySearchUseCase,
    private val createUserSearchUseCase: CreateUserSearchUseCase,
    private val deleteUserSearchUseCase: DeleteUserSearchUseCase,
): ViewModel() {
    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    private val _searchState = MutableStateFlow<FeatureState<List<Search>>?>(null)
    val searchState: StateFlow<FeatureState<List<Search>>?> = _searchState

    private val _display = MutableStateFlow<Boolean>(false)
    val display: StateFlow<Boolean> = _display.asStateFlow()

    private val _userSearch = MutableStateFlow<FeatureState<List<RecentlySearch>>>(FeatureState.Loading)
    val userSearch: StateFlow<FeatureState<List<RecentlySearch>>> = _userSearch.asStateFlow()

    fun setDisplay() {
        if(_display.value) return
        _display.value = true
    }

    init {
        _currentSearch
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(200)
            .onEach { query ->
                if (query.length < 2) {
                    _searchState.value = null
                    return@onEach
                }

                _searchState.value = FeatureState.Loading
            }
            .filter { it.length >= 2 }
            .mapLatest { query ->
                withVisibleLoading {
                    searchUseCase(query)
                }
            }
            .onEach { result -> _searchState.value = result }
            .catch { e -> _searchState.value = FeatureState.Error(e) }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            loadUserSearch()
        }
    }

    fun handleSearch(query: String) {
        _currentSearch.value = query
    }

    fun clearSearch() {
        _currentSearch.value = ""
        _searchState.value = null
    }


    private suspend fun loadUserSearch() {
        _userSearch.value = FeatureState.Loading
        _userSearch.value = getRecentlySearchUseCase()
    }

    fun createSearch(keyword: String) {
        viewModelScope.launch {
            val response = createUserSearchUseCase(keyword)

            response
                .onFailure { e ->
                    Timber.tag("Search").e(e, "ERROR: on Creating User Search")
                }
                .onSuccess { newEntry ->
                    val current = _userSearch.value

                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = buildList {
                            add(newEntry)
                            addAll(current.data)
                        }.take(20)

                        _userSearch.value = FeatureState.Success(updatedRecentlySearch)
                    }
                }
        }
    }

    fun deleteUserSearch(searchId: Int) {
        viewModelScope.launch {
            val response = deleteUserSearchUseCase(searchId)

            response
                .onFailure { e ->
                    Timber.tag("Search").e(e, "ERROR: on Deleting User Search")
                }
                .onSuccess {
                    val current = _userSearch.value

                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = current.data
                            .filterNot { it.id == searchId }

                        _userSearch.value = FeatureState.Success(updatedRecentlySearch)
                    }
                }
        }
    }
}