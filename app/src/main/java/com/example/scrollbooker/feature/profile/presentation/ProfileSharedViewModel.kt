package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.profile.domain.model.UserProfile
import com.example.scrollbooker.feature.profile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.feature.user.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.feature.user.domain.useCase.UpdateBioUseCase
import com.example.scrollbooker.feature.user.domain.useCase.UpdateFullNameUseCase
import com.example.scrollbooker.feature.user.domain.useCase.UpdateGenderUseCase
import com.example.scrollbooker.feature.user.domain.useCase.UpdateUsernameUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.onSuccess

@HiltViewModel
class ProfileSharedViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateFullNameUseCase: UpdateFullNameUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateGenderUseCase: UpdateGenderUseCase
): ViewModel() {

    private val _userProfileState = MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    var isSaved by mutableStateOf(false)

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _userProfileState.value = FeatureState.Loading
            val userId = authDataStore.getUserId().firstOrNull()

            _userProfileState.value = getUserProfileUseCase(userId)
        }
    }

//    fun updateFullName(newFullName: String) {
//        viewModelScope.launch {
//            _editState.value = FeatureState.Loading
//
//            updateFullNameUseCase(newFullName)
//                .onSuccess {
//                    user = user?.copy(fullName = newFullName)
//                    _editState.value = FeatureState.Success(Unit)
//                    isSaved = true
//                }
//                .onFailure { error ->
//                    Timber.tag("EditProfile").e(error, "ERROR: on Edit FullName User Data")
//                    _editState.value = FeatureState.Error(error = null)
//                }
//        }
//    }
//
//    fun updateUsername(newUsername: String) {
//        viewModelScope.launch {
//            _editState.value = FeatureState.Loading
//
//            updateUsernameUseCase(newUsername)
//                .onSuccess {
//                    user = user?.copy(username = newUsername)
//                    _editState.value = FeatureState.Success(Unit)
//                    isSaved = true
//                }
//                .onFailure { error ->
//                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Username User Data")
//                    _editState.value = FeatureState.Error(error = null)
//                }
//        }
//    }
//
//    fun updateBio(newBio: String) {
//        viewModelScope.launch {
//            _editState.value = FeatureState.Loading
//
//            updateBioUseCase(newBio)
//                .onSuccess {
//                    user = user?.copy(bio = newBio)
//                    _editState.value = FeatureState.Success(Unit)
//                    isSaved = true
//                }
//                .onFailure { error ->
//                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Bio User Data")
//                    _editState.value = FeatureState.Error(error = null)
//                }
//        }
//    }
//
//    fun updateGender(newGender: String) {
//        viewModelScope.launch {
//            _editState.value = FeatureState.Loading
//
//            updateGenderUseCase(newGender)
//                .onSuccess {
//                    user = user?.copy(gender = newGender)
//                    _editState.value = FeatureState.Success(Unit)
//                    isSaved = true
//                }
//                .onFailure { error ->
//                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Gender")
//                    _editState.value = FeatureState.Error(error = null)
//                }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.clearUserSession()
        }
    }
}