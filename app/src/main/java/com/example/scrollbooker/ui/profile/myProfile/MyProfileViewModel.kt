package com.example.scrollbooker.ui.profile.myProfile
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBioUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateFullNameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val updateFullNameUseCase: UpdateFullNameUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateGenderUseCase: UpdateGenderUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _userProfileState = MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    private val _initCompleted = MutableStateFlow(false)
    val isInitLoading = combine(_userProfileState, _initCompleted) { profile, done ->
        (profile is FeatureState.Loading || !done)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .onEach { _initCompleted.value = true }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Refetch UserProfile").e("ERROR: on Refetching User Profile. User Id not found ")
                throw IllegalStateException("User id not found in datastore")
            }

            _userProfileState.value = FeatureState.Loading

            val response = withVisibleLoading { getUserProfileUseCase(userId) }

            _userProfileState.value = response
        }
    }

    init {
        viewModelScope.launch {
            loadUserProfile()
        }
    }

    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    var isSaved by mutableStateOf(false)

    fun setUserProfile(userProfileData: FeatureState<UserProfile>) {
        _userProfileState.value = userProfileData
    }

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
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit FullName User Data")
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
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Username User Data")
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
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Bio User Data")
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
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Gender User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }
}