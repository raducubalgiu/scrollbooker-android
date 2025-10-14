package com.example.scrollbooker.ui.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.search.domain.useCase.CreateUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.DeleteUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.GetUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedSearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val getUserSearchUseCase: GetUserSearchUseCase,
    private val createUserSearchUseCase: CreateUserSearchUseCase,
    private val deleteUserSearchUseCase: DeleteUserSearchUseCase,
): ViewModel() {
    private val _searchState = MutableStateFlow<FeatureState<List<Search>>?>(null)
    val searchState: StateFlow<FeatureState<List<Search>>?> = _searchState

    private val _currentSearch = MutableStateFlow<String>("")
    val currentSearch: StateFlow<String> = _currentSearch

    private var debounceJob: Job? = null

    fun handleSearch(query: String, lng: Float = 25.993102.toFloat(), lat: Float = 44.450507.toFloat()) {
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

    private val _userSearch = MutableStateFlow<FeatureState<UserSearch>>(FeatureState.Loading)
    val userSearch: StateFlow<FeatureState<UserSearch>> = _userSearch

    private suspend fun loadUserSearch() {
        _userSearch.value = FeatureState.Loading
        _userSearch.value = getUserSearchUseCase(
            lng = 44.45050f,
            lat = 25.993102f,
            timezone = "Europe/Bucharest"
        )
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

    init {
        viewModelScope.launch { loadUserSearch() }
    }
}