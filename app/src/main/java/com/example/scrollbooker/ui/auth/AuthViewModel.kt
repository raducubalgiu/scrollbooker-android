package com.example.scrollbooker.ui.auth
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
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
    private val tokenProvider: TokenProvider,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val verifyUserEmailUseCase: VerifyUserEmailUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<FeatureState<AuthState>>(FeatureState.Loading)
    val authState: StateFlow<FeatureState<AuthState>> = _authState.asStateFlow()

    private val _verifyEmailState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val verifyEmailState: StateFlow<FeatureState<Unit>> = _verifyEmailState.asStateFlow()

    init {
        checkIsLoggedIn()
    }

    fun checkIsLoggedIn() {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value = isLoggedInUseCase()
        }
    }

    fun loginWithGoogle(context: Context, webClientId: String) {
//        viewModelScope.launch {
//            _authState.value = FeatureState.Loading
//
//            val credentialManager = CredentialManager.create(context)
//
//            // Configurăm opțiunea de autentificare cu Google
//            val googleIdOption = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(false) // Permite utilizatorului să aleagă orice cont
//                .setServerClientId(webClientId)       // ID-ul de server din Firebase/Google Cloud
//                .setAutoSelectEnabled(false)
//                .build()
//
//            val request = GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build()
//
//            try {
//                // Această linie deschide fereastra nativă Google pe ecran
//                val result = credentialManager.getCredential(context = context, request = request)
//                val credential = result.credential
//
//                if (credential is GoogleIdTokenCredential) {
//                    val idToken = credential.idToken
//
//                    // 🚀 SUCCES! Avem token-ul de la Google.
//                    // Aici vei apela UseCase-ul tău pentru a trimite idToken către FastAPI!
//                    // Exemplu: _authState.value = loginWithGoogleUseCase(idToken)
//
//                    // Momentan, simulăm succesul schimbând starea:
//                    // updateAuthState(AuthState(isValidated = true, registrationStep = null))
//
//                } else {
//                    _authState.value = FeatureState.Error(Exception("Tip de credential neașteptat"))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // Dacă utilizatorul apasă „Back” sau închide fereastra, ajunge aici
//                _authState.value = FeatureState.Error(e)
//            }
//        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading

            _authState.value = withVisibleLoading {
                loginUseCase(username, password)
            }
        }
    }

    fun register(
        email: String,
        password: String,
        roleName: RoleNameEnum
    ) {
        viewModelScope.launch {
            _authState.value = FeatureState.Loading
            _authState.value =
                withVisibleLoading {
                    registerUseCase(
                        email,
                        password,
                        roleName
                    )
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