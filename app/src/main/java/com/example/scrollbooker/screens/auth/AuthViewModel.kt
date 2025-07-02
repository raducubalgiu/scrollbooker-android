package com.example.scrollbooker.screens.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.useCase.IsLoggedInUseCase
import com.example.scrollbooker.entity.auth.domain.useCase.LoginUseCase
import com.example.scrollbooker.entity.auth.domain.useCase.RegisterUseCase
import com.example.scrollbooker.entity.user.userEmailVerify.domain.useCase.VerifyUserEmailUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val verifyUserEmailUseCase: VerifyUserEmailUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<FeatureState<AuthState>>(FeatureState.Loading)
    val authState: StateFlow<FeatureState<AuthState>> = _authState.asStateFlow()

    private val _verifyEmailState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val verifyEmailState: StateFlow<FeatureState<Unit>> = _verifyEmailState.asStateFlow()

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
            _authState.value = loginUseCase(username, password)
        }
    }

    fun register(
        email: String,
        password: String,
        roleName: RoleNameEnum
    ) {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value = registerUseCase(
                email,
                password,
                roleName
            )
        }
    }

    fun verifyEmail() {
        viewModelScope.launch {
            _verifyEmailState.value = FeatureState.Loading
            _verifyEmailState.value = verifyUserEmailUseCase()
        }
    }

    fun updateAuthState(authState: AuthState) {
        _authState.value = FeatureState.Success(authState)
    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.clearUserSession()
            _authState.value = FeatureState.Success(
                AuthState(
                    isValidated = false,
                    registrationStep = null
                )
            )
        }
    }
}