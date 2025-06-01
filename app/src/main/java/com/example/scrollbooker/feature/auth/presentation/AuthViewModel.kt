package com.example.scrollbooker.feature.auth.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _loginState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val loginState: StateFlow<FeatureState<Unit>> = _loginState.asStateFlow()

    init {
        checkIsLoggedIn()
    }

    fun checkIsLoggedIn() {
        viewModelScope.launch {
            _loginState.value = FeatureState.Loading

            val isLoggedIn = authRepository.isLoggedIn()

            if(isLoggedIn) {
                _loginState.value = FeatureState.Success(Unit)
            } else {
                _loginState.value = FeatureState.Error(R.string.userNotLoggedIn)
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = FeatureState.Loading

            val result = authRepository.loginAndSaveUserSession(
                LoginRequest(username = username, password = password)
            )

            _loginState.value = result.fold(
                onSuccess = { FeatureState.Success(Unit) },
                onFailure = {
                    Timber.tag("Login").e(it, "ERROR: Login Failed")
                    FeatureState.Error(R.string.somethingWentWrong)
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            checkIsLoggedIn()
        }
    }
}