package com.example.scrollbooker.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.usecase.LoginUseCase
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle: LoginState()
    object Loading: LoginState()
    object Success: LoginState()
    data class Error(val message: String): LoginState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                val isLoggedIn = isTokenValid()
                _loginState.value = if (isLoggedIn) LoginState.Success else LoginState.Idle
            } catch (e: Exception) {
                _loginState.value = LoginState.Idle
            } finally {
                _isInitialized.value = true
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = loginUseCase(LoginRequest(username, password))
            result.fold(
                onSuccess = {
                    authDataStore.saveTokens(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken,
                        userId = it.userId,
                        businessId = it.businessId
                    )
                    _loginState.value = LoginState.Success
                },
                onFailure = {
                    _loginState.value = LoginState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return isTokenValid()
    }

    private suspend fun isTokenValid(): Boolean {
        val token = authDataStore.getAccessToken().firstOrNull()
        val expiry = token?.let { decodeJwtExpiry(it) }
        return expiry != null && System.currentTimeMillis() < expiry
    }
}