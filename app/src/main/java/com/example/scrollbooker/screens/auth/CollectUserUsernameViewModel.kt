package com.example.scrollbooker.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.SearchUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectUserUsernameViewModel @Inject constructor(
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val searchUsernameUseCase: SearchUsernameUseCase
): ViewModel() {
    private val _searchState = MutableStateFlow<FeatureState<SearchUsernameResponse>?>(null)
    val searchState: StateFlow<FeatureState<SearchUsernameResponse>?> = _searchState

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _currentUsername = MutableStateFlow("")
    val currentUsername: StateFlow<String> = _currentUsername

    private val _navigateToNextStep = MutableStateFlow(false)
    val navigateToNextStep: StateFlow<Boolean> = _navigateToNextStep

    private var debounceJob: Job? = null

    fun searchUsername(username: String) {
        _currentUsername.value = username

        if(username.length < 3) {
            debounceJob?.cancel()
            _searchState.value = null
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)

            val latest = currentUsername.value
            if(latest.length < 3 || latest != username) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchUsernameUseCase(username)
            }
       }
    }

    fun collectUserUsername(newUsername: String) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading
            delay(300)

            updateUsernameUseCase(username = newUsername)
                .onFailure { e ->
                    _isSaving.value = FeatureState.Error(e)
                    _navigateToNextStep.value = false
                    Timber.tag("Update username").e("ERROR: on updating Username $e")
                }
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)
                    _navigateToNextStep.value = true
                }
        }
    }
}