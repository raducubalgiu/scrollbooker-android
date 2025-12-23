package com.example.scrollbooker.ui.inbox
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.enums.ConsentEnum
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.cachedByKey
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase.GetEmploymentRequestByIdUseCase
import com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase.RespondEmploymentRequestUseCase
import com.example.scrollbooker.entity.nomenclature.consent.domain.model.Consent
import com.example.scrollbooker.entity.nomenclature.consent.domain.useCase.GetConsentsByNameUseCase
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.useCase.GetNotificationsUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class InboxViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val respondEmploymentRequestUseCase: RespondEmploymentRequestUseCase,
    private val getConsentsByNameUseCase: GetConsentsByNameUseCase,
    private val getEmploymentRequestByIdUseCase: GetEmploymentRequestByIdUseCase
): ViewModel() {
    private val _notificationRefreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: Flow<PagingData<Notification>> = _notificationRefreshTrigger
        .flatMapLatest { getNotificationsUseCase() }
        .cachedIn(viewModelScope)

    private val _employmentRequestId = MutableStateFlow<Int?>(null)
    val employmentRequestId: StateFlow<Int?> = _employmentRequestId.asStateFlow()

    private val _followState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val followState = _followState.asStateFlow()

    private val _agreedTerms = MutableStateFlow<Boolean>(false)
    val agreedTerms: StateFlow<Boolean> = _agreedTerms

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _isFollowSaving = MutableStateFlow<Set<Int>>(emptySet())
    val isFollowSaving = _isFollowSaving.asStateFlow()

    fun refreshNotifications() {
        _notificationRefreshTrigger.value += 1
    }

    fun follow(isFollow: Boolean, userId: Int) {
        if(_isFollowSaving.value.contains(userId)) {
            return
        }

        _isFollowSaving.update { it + userId }
        _followState.update { it + (userId to !isFollow) }

        viewModelScope.launch {
            try {
                if(isFollow) {
                    unfollowUserUseCase(userId)
                } else {
                    followUserUseCase(userId)
                }
            } catch(e: Exception) {
                _followState.update { it + (userId to isFollow) }
                Timber.tag("Follow/Unfollow").e("ERROR: $e")

            } finally {
                _isFollowSaving.update { it - userId }
            }
        }
    }

    private val consentKey = ConsentEnum.EMPLOYMENT_REQUESTS_INITIATION.key

    private val cachedConsent = cachedByKey(
        scope = viewModelScope,
        keyFlow = flowOf(consentKey),
    ) { name -> withVisibleLoading { getConsentsByNameUseCase(name) } }

    val consentState: StateFlow<FeatureState<Consent>> = cachedConsent.first
    private val refreshConsentKey = cachedConsent.second

    fun loadConsent() {
        viewModelScope.launch { refreshConsentKey(consentKey) }
    }

    private val cachedEmployment = cachedByKey(
        scope = viewModelScope,
        keyFlow = employmentRequestId,
    ) { id -> withVisibleLoading { getEmploymentRequestByIdUseCase(id) } }

    val employmentRequestState: StateFlow<FeatureState<EmploymentRequest>> = cachedEmployment.first
    private val refreshEmploymentKey = cachedEmployment.second

    fun loadEmploymentRequest() {
        val id = _employmentRequestId.value ?: run {
            Timber.tag("Employment Requests").e("ERROR: Employment Id is not defined")
            return
        }

        viewModelScope.launch { refreshEmploymentKey(id) }
    }

    fun setEmploymentId(employmentId: Int) {
        _employmentRequestId.value = employmentId
    }

    fun setAgreed() {
        _agreedTerms.value = !_agreedTerms.value
    }

    suspend fun respondToRequest(status: EmploymentRequestStatusEnum): Result<Unit> {
        _isSaving.value = true
        val employmentId = _employmentRequestId.value

        if(employmentId == null) {
            return Result.failure(IllegalStateException("EmploymentId is not defined"))
        }

        val response = withVisibleLoading {
            respondEmploymentRequestUseCase(status, employmentId)
        }

        response
            .onFailure { e ->
                Timber.tag("Employment Request").e("ERROR: on Responding Employment Request $e")
                _isSaving.value = false
            }
            .onSuccess {
                refreshNotifications()
                _isSaving.value = false
            }

        return response
    }
}