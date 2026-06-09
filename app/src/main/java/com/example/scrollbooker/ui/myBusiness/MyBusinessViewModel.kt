package com.example.scrollbooker.ui.myBusiness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBusinessViewModel @Inject constructor(
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            withVisibleLoading(minLoadingMs = 300L) {
                authDataStore.getHasEmployees().first()
            }
            _isLoading.value = false
        }
    }


    val hasEmployees: StateFlow<Boolean> = authDataStore.getHasEmployees()
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val isEmployee: StateFlow<Boolean> = combine(
        authDataStore.getUserId(),
        authDataStore.getBusinessOwnerId()
    ) { userId, businessOwnerId ->
        if (userId != null && businessOwnerId != null) {
            userId != businessOwnerId
        } else {
            false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )
}