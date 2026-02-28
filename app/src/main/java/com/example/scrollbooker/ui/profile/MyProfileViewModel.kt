package com.example.scrollbooker.ui.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.user.userProfile.data.remote.toUserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
    private val updateWebsiteUseCase: UpdateWebsiteUseCase,
    private val updatePublicEmailUseCase: UpdatePublicEmailUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val authDataStore: AuthDataStore,
    @ApplicationContext private val app: Context,
): ViewModel() {
    private val _profile =
        MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val profile: StateFlow<FeatureState<UserProfile>> = _profile.asStateFlow()

    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.Forest.tag("Refetch UserProfile").e("ERROR: on Refetching User Profile. User Id not found ")
                throw IllegalStateException("User id not found in datastore")
            }

            _profile.value = FeatureState.Loading

            val response = withVisibleLoading {
                getUserProfileUseCase(userId, lat = null, lng = null)
            }

            _profile.value = response
        }
    }

    init {
        loadUserProfile()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getEmployeesByOwnerUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarks: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getUserBookmarkedPostsUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val about: StateFlow<FeatureState<UserProfileAbout>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)
                emit(withVisibleLoading { getUserProfileAboutUseCase(userId) })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedules: StateFlow<FeatureState<List<Schedule>>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getSchedulesByUserIdUseCase(userId)
                }

                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    // Edit
    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    fun setPhoto(uri: Uri) {
        _photoUri.value = uri
    }

    var isSaved by mutableStateOf(false)

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateFullNameUseCase(newFullName) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(fullName = newFullName)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(username = newUsername)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(bio = newBio)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(gender = newGender)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(website = newWebsite)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(publicEmail = newPublicEmail)
                        _profile.value = FeatureState.Success(updatedProfile)
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

                    val currentProfile = (_profile.value as? FeatureState.Success)?.data

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(avatar = uri.toString())
                        _profile.value = FeatureState.Success(updatedProfile)
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