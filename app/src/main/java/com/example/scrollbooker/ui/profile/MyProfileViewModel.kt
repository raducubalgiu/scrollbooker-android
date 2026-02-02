package com.example.scrollbooker.ui.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.user.userProfile.data.remote.toUserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateAvatarUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBioUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateFullNameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdatePublicEmailUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateWebsiteUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val updateFullNameUseCase: UpdateFullNameUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateGenderUseCase: UpdateGenderUseCase,
    private val updateWebsiteUseCase: UpdateWebsiteUseCase,
    private val updatePublicEmailUseCase: UpdatePublicEmailUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val authDataStore: AuthDataStore,
    @ApplicationContext private val app: Context,
): ViewModel() {
    private val _userProfileState =
        MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.Forest.tag("Refetch UserProfile").e("ERROR: on Refetching User Profile. User Id not found ")
                throw IllegalStateException("User id not found in datastore")
            }

            _userProfileState.value = FeatureState.Loading

            val response = withVisibleLoading {
                getUserProfileUseCase(userId, lat = null, lng = null)
            }

            _userProfileState.value = response
        }
    }

    init {
        loadUserProfile()
    }

    // Edit
    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    fun setPhoto(uri: Uri) {
        _photoUri.value = uri
    }

    var isSaved by mutableStateOf(false)

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateFullNameUseCase(newFullName) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(fullName = newFullName)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit FullName User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = updateUsernameUseCase(newUsername)

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(username = newUsername)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Username User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateBio(newBio: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateBioUseCase(newBio) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(bio = newBio)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Bio User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateGender(newGender: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateGenderUseCase(newGender) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(gender = newGender)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Gender User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateWebsite(newWebsite: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateWebsiteUseCase(newWebsite) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(website = newWebsite)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Website User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updatePublicEmail(newPublicEmail: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updatePublicEmailUseCase(newPublicEmail) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(publicEmail = newPublicEmail)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Public Email User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val request = runCatching { uri.toUserAvatarRequest(app.contentResolver) }
                .getOrElse { e ->
                    _editState.value = FeatureState.Error(e)
                    return@launch
                }

            val result = withVisibleLoading { updateAvatarUseCase(request) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(avatar = uri.toString())
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit User Avatar")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }
}