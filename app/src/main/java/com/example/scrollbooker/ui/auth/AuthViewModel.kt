package com.example.scrollbooker.ui.auth
import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.extensions.toFeatureState
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.GoogleCredentialProvider
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.useCase.IsLoggedInUseCase
import com.example.scrollbooker.entity.auth.domain.useCase.LoginUseCase
import com.example.scrollbooker.entity.auth.domain.useCase.RegisterUseCase
import com.example.scrollbooker.entity.auth.domain.useCase.SignInWithGoogleUseCase
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
    private val tokenProvider: TokenProvider,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val verifyUserEmailUseCase: VerifyUserEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val googleCredentialProvider: GoogleCredentialProvider
): ViewModel() {
    private val _authState = MutableStateFlow<FeatureState<AuthState>>(FeatureState.Loading)
    val authState: StateFlow<FeatureState<AuthState>> = _authState.asStateFlow()

    private val _isCredentialsAuthLoading = MutableStateFlow(false)
    val isCredentialsAuthLoading: StateFlow<Boolean> = _isCredentialsAuthLoading.asStateFlow()

    private val _isGoogleAuthLoading = MutableStateFlow(false)
    val isGoogleAuthLoading: StateFlow<Boolean> = _isGoogleAuthLoading.asStateFlow()

    private val _verifyEmailState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val verifyEmailState: StateFlow<FeatureState<Unit>> = _verifyEmailState.asStateFlow()

    init {
        checkIsLoggedIn()
    }

    fun checkIsLoggedIn() {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value = isLoggedInUseCase().toFeatureState()
        }
    }

    fun sigInWithGoogle(activity: Activity, webClientId: String, roleName: RoleNameEnum) {
        viewModelScope.launch {
            _isGoogleAuthLoading.value = true
            try {
                _authState.value = googleCredentialProvider.getIdToken(activity, webClientId)
                    .mapCatching { idToken -> signInWithGoogleUseCase(idToken, roleName).getOrThrow() }
                    .toFeatureState()
            } finally {
                _isGoogleAuthLoading.value = false
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isCredentialsAuthLoading.value = true
            try {
                _authState.value = withVisibleLoading {
                    loginUseCase(username, password).toFeatureState()
                }
            } finally {
                _isCredentialsAuthLoading.value = false
            }
        }
    }

    fun register(email: String, password: String, roleName: RoleNameEnum) {
        viewModelScope.launch {
            _isCredentialsAuthLoading.value = true
            try {
                _authState.value = withVisibleLoading {
                    registerUseCase(email, password, roleName).toFeatureState()
                }
            } finally {
                _isCredentialsAuthLoading.value = false
            }
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

    suspend fun logout(): Result<Unit> = runCatching {
        authDataStore.clearUserSession()
        tokenProvider.clearTokens()

        _authState.value = FeatureState.Success(
            AuthState(
                isValidated = false,
                registrationStep = null
            )
        )
    }
}