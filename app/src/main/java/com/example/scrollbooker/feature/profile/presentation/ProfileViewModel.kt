package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.feature.profile.domain.model.User
import com.example.scrollbooker.feature.profile.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadUser() {
        viewModelScope.launch {
            isLoading = true

            try {
                user = getUserInfoUseCase()
            } catch (e: Exception) {
                SnackbarManager.showError("Ceva nu a mers cum trebuie. Încearcă mai târziu")
                Timber.tag("Profile").e(e, "ERROR: on Loading Profile User Data")
            } finally {
                isLoading = false
            }
        }
    }

    init {
        loadUser()
    }
}