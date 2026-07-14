package com.example.scrollbooker.ui.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.entity.user.userProfile.data.remote.toUserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.SearchUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateAvatarUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBioUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBirthDateUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateFullNameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdatePublicEmailUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateWebsiteUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    authDataStore: AuthDataStore,
    private val updateFullNameUseCase: UpdateFullNameUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateGenderUseCase: UpdateGenderUseCase,
    private val updateBirthDateUseCase: UpdateBirthDateUseCase,
    private val updateWebsiteUseCase: UpdateWebsiteUseCase,
    private val updatePublicEmailUseCase: UpdatePublicEmailUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val searchUsernameUseCase: SearchUsernameUseCase,
    @ApplicationContext private val app: Context,

    getUserProfileUseCase: GetUserProfileUseCase,
    getUserPostsUseCase: GetUserPostsUseCase,
    getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    likePostUseCase: LikePostUseCase,
    unLikePostUseCase: UnLikePostUseCase,
    bookmarkPostUseCase: BookmarkPostUseCase,
    unBookmarkPostUseCase: UnBookmarkPostUseCase,
    videoPlayerManager: VideoPlayerManager,
):  BaseProfileViewModel(
    shouldShowVisibleLoading = false,
    getUserProfileUseCase = getUserProfileUseCase,
    getUserPostsUseCase = getUserPostsUseCase,
    getEmployeesByOwnerUseCase = getEmployeesByOwnerUseCase,
    getUserBookmarkedPostsUseCase = getUserBookmarkedPostsUseCase,
    getProductsByBusinessIdAndEmployeeIdUseCase = getProductsByBusinessIdAndEmployeeIdUseCase,
    getUserProfileAboutUseCase = getUserProfileAboutUseCase,
    getSchedulesByUserIdUseCase = getSchedulesByUserIdUseCase,
    likePostUseCase = likePostUseCase,
    unLikePostUseCase = unLikePostUseCase,
    bookmarkPostUseCase = bookmarkPostUseCase,
    unBookmarkPostUseCase = unBookmarkPostUseCase,
    videoPlayerManager = videoPlayerManager
) {
    override val userIdFlow: Flow<Int?> = authDataStore.getUserId().distinctUntilChanged()
    override val usernameFlow: Flow<String?> = authDataStore.getUserUsername().distinctUntilChanged()

    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState.asStateFlow()

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    val selectedDay = MutableStateFlow<String?>(null)
    val selectedMonth = MutableStateFlow<String?>(null)
    val selectedYear = MutableStateFlow<String?>(null)

    val isBirthDateValid: StateFlow<Boolean> = combine(
        selectedDay, selectedMonth, selectedYear
    ) { day, month, year ->
        !day.isNullOrBlank() && !month.isNullOrBlank() && !year.isNullOrBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    private val _searchState = MutableStateFlow<FeatureState<SearchUsernameResponse>?>(null)
    val searchState: StateFlow<FeatureState<SearchUsernameResponse>?> = _searchState

    private val _currentUsername = MutableStateFlow("")
    val currentUsername: StateFlow<String> = _currentUsername

    private var debounceJob: Job? = null

    fun searchUsername(username: String) {
        _currentUsername.value = username

        if(username.length < 3) {
            debounceJob?.cancel()
            _searchState.value = null
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(200)

            val latest = currentUsername.value
            if(latest.length < 3 || latest != username) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchUsernameUseCase(username)
            }
        }
    }

    var isSaved by mutableStateOf(false)

    fun setPhoto(uri: Uri) {
        _photoUri.value = uri
    }

    fun setSelectedDay(newDay: String?) {
        selectedDay.value = newDay
    }

    fun setSelectedMonth(newMonth: String?) {
        selectedMonth.value = newMonth
    }

    fun setSelectedYear(newYear: String?) {
        selectedYear.value = newYear
    }

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateFullNameUseCase(newFullName) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(fullName = newFullName)

                        profileMutations.emit(FeatureState.Success(updatedProfile))
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(username = newUsername)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(bio = newBio)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(gender = newGender)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Gender User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateBirthDate(birthDate: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateBirthDateUseCase(birthdate = birthDate) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(dateOfBirth = birthDate)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
                    }
                    isSaved = true
                }
                .onFailure { e ->
                    _editState.value = FeatureState.Error(error = null)
                    Timber.tag("Update birthdate").e("ERROR: on updating Birthdate $e")
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(website = newWebsite)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Website User Data")
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(publicEmail = newPublicEmail)
                        profileMutations.emit(FeatureState.Success(updatedProfile))
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit Public Email User Data")
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

                    val currentProfile = (profile.value as? FeatureState.Success)?.data
                    if (currentProfile != null) {
                        val updatedProfile = currentProfile.copy(avatar = uri.toString())
                        profileMutations.emit(FeatureState.Success(updatedProfile))
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.tag("EditProfile").e(error, "ERROR: on Edit User Avatar")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        videoPlayerManager.releaseScreenScope("MY_PROFILE_DETAIL_POSTS")
        videoPlayerManager.releaseScreenScope("MY_PROFILE_DETAIL_BOOKMARKS")
    }
}