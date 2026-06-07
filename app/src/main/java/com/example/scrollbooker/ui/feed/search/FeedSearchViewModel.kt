package com.example.scrollbooker.ui.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.search.domain.model.SearchUser
import com.example.scrollbooker.entity.search.domain.useCase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class FeedSearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
): ViewModel() {
    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    private val _searchState = MutableStateFlow<FeatureState<List<SearchUser>>?>(null)
    val searchState: StateFlow<FeatureState<List<SearchUser>>?> = _searchState

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
                    searchUsersUseCase(query, roleClient = null)
                }
            }
            .onEach { result -> _searchState.value = result }
            .catch { e -> _searchState.value = FeatureState.Error(e) }
            .launchIn(viewModelScope)
    }

    fun handleSearch(query: String) {
        _currentSearch.value = query
    }
}

