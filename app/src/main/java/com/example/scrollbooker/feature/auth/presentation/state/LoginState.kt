package com.example.scrollbooker.feature.auth.presentation.state

import com.example.scrollbooker.feature.auth.domain.model.LoginResponse

sealed class LoginState {
    object Idle: LoginState()
    object Loading: LoginState()
    data class Success(val data: LoginResponse): LoginState()
    data class Error(val message: String): LoginState()
}
