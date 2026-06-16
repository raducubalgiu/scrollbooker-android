package com.example.scrollbooker.ui.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.customized.post.VideoPlayerCache
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.useCase.RefreshTokenUseCase
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.entity.user.userProfile.data.remote.toUserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plus
import kotlin.collections.set

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
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    private val searchUsernameUseCase: SearchUsernameUseCase,
    private val videoPlayerManager: VideoPlayerManager,
    @ApplicationContext private val app: Context,
): ViewModel(), ProfilePostDetailViewModelContract {
    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val profileMutations = MutableSharedFlow<FeatureState<UserProfile>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val profile: StateFlow<FeatureState<UserProfile>> = merge(
        authDataStore.getUserUsername()
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUsername ->
                flow {
                    emit(FeatureState.Loading)

                    val response = withVisibleLoading {
                        getUserProfileUseCase(currentUsername, lat = null, lng = null)
                    }
                    emit(response)
                }
            },
        profileMutations
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FeatureState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getUserPostsUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getEmployeesByOwnerUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val bookmarks: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getUserBookmarkedPostsUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: StateFlow<FeatureState<UserProducts>> = profile
        .mapNotNull { state -> if (state is FeatureState.Success) state.data else null }
        .mapNotNull { userProfile ->
            val businessId = userProfile.businessId ?: return@mapNotNull null
            val isEmployee = userProfile.businessOwner?.id != userProfile.id
            val employeeId = if (isEmployee) userProfile.id else null

            Pair(businessId, employeeId)
        }
        .distinctUntilChanged()
        .flatMapLatest { (businessId, employeeId) ->
            flow {
                emit(FeatureState.Loading)
                val result = withVisibleLoading {
                    getProductsByBusinessIdAndEmployeeIdUseCase(
                        businessId = businessId,
                        employeeId = employeeId,
                        onlyServicesWithProducts = true,
                        productsLimitPerService = null
                    )
                }
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val about: StateFlow<FeatureState<UserProfileAbout>> = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId ->
            flow {
                emit(FeatureState.Loading)
                emit(withVisibleLoading { getUserProfileAboutUseCase(currentUserId) })
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
        .flatMapLatest { currentUserId ->
            flow {
                emit(FeatureState.Loading)
                val result = withVisibleLoading { getSchedulesByUserIdUseCase(currentUserId) }
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

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

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
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

    private val _postUi = MutableStateFlow<Map<Int, PostActionUiState>>(emptyMap())
    override fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        _postUi.map { it[postId] ?: PostActionUiState.EMPTY }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PostActionUiState.EMPTY)

    private inline fun MutableStateFlow<Map<Int, PostActionUiState>>.edit(
        postId: Int,
        crossinline reducer: (PostActionUiState) -> PostActionUiState
    ) {
        update { map ->
            val curr = map[postId] ?: PostActionUiState.EMPTY
            map + (postId to reducer(curr))
        }
    }

    private inline fun toggleAction(
        postId: Int,
        backendFlag: Boolean,
        backendCount: Int,
        crossinline isFlagOverridden: (PostActionUiState) -> Boolean?,
        crossinline setFlag: (PostActionUiState, Boolean) -> PostActionUiState,
        crossinline savingOn: (PostActionUiState) -> PostActionUiState,
        crossinline savingOff: (PostActionUiState) -> PostActionUiState,
        crossinline getDelta: (PostActionUiState) -> Int,
        crossinline setDelta: (PostActionUiState, Int) -> PostActionUiState,
        crossinline doOn: suspend () -> Result<Unit>,
        crossinline doOff: suspend () -> Result<Unit>,
    ) {
        val currentPostAction = _postUi.value[postId] ?: PostActionUiState.EMPTY

        val currentFlag = isFlagOverridden(currentPostAction) ?: backendFlag

        val wantOn = !currentFlag

        _postUi.edit(postId) { s ->
            val base = backendCount
            val curr = base + getDelta(s)
            val next = if(wantOn) curr + 1 else curr - 1

            savingOn(setFlag(setDelta(s, next - base), wantOn))
        }

        viewModelScope.launch {
            val result = if(wantOn) doOn() else doOff()

            if(result.isSuccess) {
                _postUi.edit(postId) { s -> savingOff(s) }
            } else {
                _postUi.edit(postId) { s ->
                    val revertedFlag = !(isFlagOverridden(s)!!)
                    val reverted = setFlag(setDelta(s, 0), revertedFlag)
                    savingOff(reverted)
                }
            }
        }
    }

    override fun toggleLike(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isLiked,
            backendCount = post.counters.likeCount,
            isFlagOverridden = { it.isLiked },
            setFlag = { s, v -> s.copy(isLiked = v) },
            savingOn = { it.copy(isSavingLike = true) },
            savingOff = { it.copy(isSavingLike = false) },
            getDelta = { it.likesCount },
            setDelta = { s, d -> s.copy(likesCount = d) },
            doOn = { likePostUseCase(post.id) },
            doOff = { unLikePostUseCase(post.id) }
        )
    }

    override fun toggleBookmark(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isBookmarked,
            backendCount = post.counters.bookmarkCount,
            isFlagOverridden = { it.isBookmarked },
            setFlag = { s, v -> s.copy(isBookmarked = v) },
            savingOn = { it.copy(isSavingBookmark = true) },
            savingOff = { it.copy(isSavingBookmark = false) },
            getDelta = { it.bookmarksCount },
            setDelta = { s, d -> s.copy(bookmarksCount = d) },
            doOn = { bookmarkPostUseCase(post.id) },
            doOff = { unBookmarkPostUseCase(post.id) }
        )
    }

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    val userPausedPostIds = videoPlayerManager.userPausedPostIds

    override fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return videoPlayerManager.getPlayerForIndex(scopeKey, index)
    }

    /**
     * Pregătește buffer-ul asincron (fereastra glisantă) DOAR când suntem în ecranul de detalii active
     */
    override fun setDetailScreenActive(isActive: Boolean, scopeKey: String, centerIndex: Int, getPost: (Int) -> Post?) {
        if (isActive) {
            // Dezactivează orice alt video activ din aplicație (ex: din Feed)
            videoPlayerManager.activateScreenScope(scopeKey)
        }
        videoPlayerManager.ensureWindow(scopeKey, centerIndex, isActive, getPost)
    }

    override fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?) {
        _currentIndex.value = index
        videoPlayerManager.onPageSettled(scopeKey, index, true)
        videoPlayerManager.ensureWindow(scopeKey, index, true, getPost)
    }

    override fun togglePlayPause(scopeKey: String, index: Int) {
        videoPlayerManager.togglePlayer(scopeKey, index)
    }

    override fun onDetailSessionFinished(scopeKey: String) {
        videoPlayerManager.releaseScreenScope(scopeKey)
    }

    override fun onCleared() {
        super.onCleared()
        videoPlayerManager.releaseScreenScope("MY_PROFILE_DETAIL_POSTS")
        videoPlayerManager.releaseScreenScope("MY_PROFILE_DETAIL_BOOKMARKS")
    }
}