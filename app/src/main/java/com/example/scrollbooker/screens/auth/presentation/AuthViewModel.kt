package com.example.scrollbooker.screens.auth.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.auth.domain.repository.AuthRepository
import com.example.scrollbooker.screens.auth.domain.usecase.IsLoggedInUseCase
import com.example.scrollbooker.screens.auth.domain.usecase.LoginAndSaveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val loginAndSaveSessionUseCase: LoginAndSaveSessionUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val authState: StateFlow<FeatureState<Unit>> = _authState.asStateFlow()

    init {
        checkIsLoggedIn()
    }

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

    fun logout() {
        viewModelScope.launch {
            //authRepository.logout()
            //checkIsLoggedIn()
            _authState.value = FeatureState.Error()
        }
    }
}