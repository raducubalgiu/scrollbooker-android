package com.example.scrollbooker.ui.onboarding.shared.collectUsername

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.useCase.RefreshTokenUseCase
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectUserUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.SearchUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectUserUsernameViewModel @Inject constructor(
    private val collectUserUsernameUseCase: CollectUserUsernameUseCase,
    private val searchUsernameUseCase: SearchUsernameUseCase,
    private val authDataStore: AuthDataStore,
    private val refreshTokenUseCase: RefreshTokenUseCase,
): ViewModel() {
    private val _searchState = MutableStateFlow<FeatureState<SearchUsernameResponse>?>(null)
    val searchState: StateFlow<FeatureState<SearchUsernameResponse>?> = _searchState

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _currentUsername = MutableStateFlow("")
    val currentUsername: StateFlow<String> = _currentUsername

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
            delay(200)

            val latest = currentUsername.value
            if(latest.length < 3 || latest != username) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchUsernameUseCase(username)
            }
       }
    }

    suspend fun collectUserUsername(newUsername: String): AuthState? {
        _isSaving.value = FeatureState.Loading

        val result = withVisibleLoading { collectUserUsernameUseCase(username = newUsername) }

        return result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update username").e("ERROR: on updating Username $e")
            }
            .onSuccess {
                val refreshToken = authDataStore.getRefreshToken().firstOrNull()

                if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
                    val response = refreshTokenUseCase(refreshToken)

                    if(response is FeatureState.Error) {
                        return AuthState(
                            isValidated = false,
                            registrationStep = null
                        )
                    }
                }

                _isSaving.value = FeatureState.Success(Unit)
            }
            .getOrNull()
    }

    private fun isTokenValid(token: String?): Boolean {
        val expiry = token?.let { decodeJwtExpiry(it) }
        return expiry != null && System.currentTimeMillis() < expiry
    }
}