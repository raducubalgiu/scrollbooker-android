package com.example.scrollbooker.screens.feed.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedSearchViewModel @Inject constructor(): ViewModel() {

    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    fun updateSearch(search: String) {
        _currentSearch.value = search
    }

    fun clearSearch() {
        _currentSearch.value = ""
    }
}