package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.selectEmployee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.user.userProfile.domain.usecase.SearchUsersClientsUseCase
import com.example.scrollbooker.shared.user.userSocial.domain.model.UserSocial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmploymentSelectEmployeeViewModel @Inject constructor(
    private val searchUsersClientsUseCase: SearchUsersClientsUseCase
): ViewModel() {

    private val _searchUsersClientsState = MutableStateFlow<FeatureState<List<UserSocial>>?>(null)
    val searchUsersClientsState: StateFlow<FeatureState<List<UserSocial>>?> = _searchUsersClientsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        observeSearchQuery()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(500L)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .collect { query ->
                    performSearch(query)
                }
        }
    }

    private suspend fun performSearch(query: String) {
        _searchUsersClientsState.value = FeatureState.Loading
        //_searchUsersClientsState.value = searchUsersClientsUseCase(query)
    }
}