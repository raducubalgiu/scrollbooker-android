package com.example.scrollbooker.screens.feed.search

import androidx.lifecycle.ViewModel
import com.example.scrollbooker.entity.search.domain.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedSearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
): ViewModel() {
    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    fun updateSearch(search: String) {
        _currentSearch.value = search
    }

    fun clearSearch() {
        _currentSearch.value = ""
    }
}