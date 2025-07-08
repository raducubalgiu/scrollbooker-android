package com.example.scrollbooker.screens.profile.myBusiness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyBusinessViewModel @Inject constructor(
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _permissionsState = MutableStateFlow<FeatureState<List<PermissionEnum>>>(
        FeatureState.Loading)
    val permissionsState: StateFlow<FeatureState<List<PermissionEnum>>> = _permissionsState

    init {
        loadPermissions()
    }

    fun loadPermissions() {
        viewModelScope.launch {
            try {
                val results = withVisibleLoading {
                    val rawList = authDataStore.getUserPermissions().first()
                    PermissionEnum.fromKeys(rawList)
                }
                _permissionsState.value = FeatureState.Success(results)
            } catch (e: Exception) {
                Timber.tag("Permissions dataStore").e("ERROR: on loading permission from authDataStore $e")
                FeatureState.Error()
            }
        }
    }

}