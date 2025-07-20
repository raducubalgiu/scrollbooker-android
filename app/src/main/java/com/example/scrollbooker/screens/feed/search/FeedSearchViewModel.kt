package com.example.scrollbooker.screens.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedSearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
): ViewModel() {
    private val _searchState = MutableStateFlow<FeatureState<List<Search>>?>(null)
    val searchState: StateFlow<FeatureState<List<Search>>?> = _searchState

    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    private var debounceJob: Job? = null

    fun handleSearch(query: String, lat: Float, lng: Float) {
        _currentSearch.value = query

        if(query.length < 2) {
            debounceJob?.cancel()
            _currentSearch.value = ""
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(200)

            val latest = currentSearch.value
            if(latest.length < 2 || latest != query) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchUseCase(query, lat, lng)
            }
        }
    }

    fun clearSearch() {
        _currentSearch.value = ""
        _searchState.value = null
    }
}