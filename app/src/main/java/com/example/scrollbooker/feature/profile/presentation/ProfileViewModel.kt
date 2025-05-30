package com.example.scrollbooker.feature.profile.presentation
import android.util.Log
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
                Log.d("Profile Request", "Start")
                user = getUserInfoUseCase()
            } catch (e: Exception) {
                SnackbarManager.showMessage(
                    scope = this,
                    message = "Something went wrong"
                )
                Log.d("Profile Request Error", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }

    init {
        loadUser()
    }
}