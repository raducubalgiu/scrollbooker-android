package com.example.scrollbooker.feature.myBusiness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyBusinessViewModel @Inject constructor(
    private val authDataStore: AuthDataStore
): ViewModel() {
    val permissions: StateFlow<List<PermissionEnum>> = authDataStore.getUserPermissions()
        .map { rawList -> PermissionEnum.fromKeys(rawList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}