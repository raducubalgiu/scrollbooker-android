package com.example.scrollbooker.screens.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.auth.domain.usecase.IsLoggedInUseCase
import com.example.scrollbooker.screens.auth.domain.usecase.LoginAndSaveSessionUseCase
import com.example.scrollbooker.screens.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthStateDto(
    val isValidated: Boolean
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginAndSaveSessionUseCase: LoginAndSaveSessionUseCase,
    private val registerUseCase: RegisterUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<FeatureState<AuthStateDto>>(FeatureState.Loading)
    val authState: StateFlow<FeatureState<AuthStateDto>> = _authState.asStateFlow()

    private val _registerState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val registerState: StateFlow<FeatureState<Unit>> = _registerState

    init { checkIsLoggedIn() }

    fun checkIsLoggedIn() {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value = isLoggedInUseCase()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value = loginAndSaveSessionUseCase(username, password)
        }
    }

    fun register(
        email: String,
        username: String,
        password: String,
        roleName: String,
        isValidated: Boolean
    ) {
        viewModelScope.launch {
            _registerState.value = FeatureState.Loading
            _registerState.value = registerUseCase(
                email,
                username,
                password,
                roleName,
                isValidated
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            //authRepository.logout()
            //checkIsLoggedIn()
            _authState.value = FeatureState.Error()
        }
    }
}