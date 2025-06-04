package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.feature.user.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.feature.user.domain.useCase.UpdateFullNameUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.onSuccess

@HiltViewModel
class ProfileSharedViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateFullNameUseCase: UpdateFullNameUseCase
): ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    var isSaved by mutableStateOf(false)

    fun loadUser() {
        viewModelScope.launch {
            isLoading = true

            try {
                user = getUserInfoUseCase()
            } catch (e: Exception) {
                //SnackbarManager.showError("Ceva nu a mers cum trebuie. Încearcă mai târziu")
                Timber.tag("Profile").e(e, "ERROR: on Loading Profile User Data")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading
            delay(500)

            updateFullNameUseCase(newFullName)
                .onSuccess {
                    user = user?.copy(fullName = newFullName)
                    _editState.value = FeatureState.Success(Unit)
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit FullName User Data")
                    _editState.value = FeatureState.Error(error)
                }
        }
    }

    fun updateUsername(newUsername: String) {
        user = user?.copy(username = newUsername)
    }

    fun updateBio(newBio: String) {
        user = user?.copy(bio = newBio)
    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.clearUserSession()
        }
    }

    init {
        Timber.tag("Init").e("-> Profile - View Model Created")
        loadUser()
    }
}