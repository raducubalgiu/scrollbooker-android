package com.example.scrollbooker.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.usecase.LoginUseCase
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
                },
                onFailure = {
                    _loginState.value = LoginState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return authDataStore.getAccessToken().firstOrNull()?.isNotBlank() == true
    }
}