package com.example.scrollbooker.screens.inbox.employmentRequestRespond
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ConsentEnum
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.consent.domain.model.Consent
import com.example.scrollbooker.entity.consent.domain.useCase.GetConsentsByNameUseCase
import com.example.scrollbooker.entity.employmentRequest.domain.useCase.RespondEmploymentRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmploymentRequestRespondViewModel @Inject constructor(
    private val respondEmploymentRequestUseCase: RespondEmploymentRequestUseCase,
    private val getConsentsByNameUseCase: GetConsentsByNameUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val employmentId: Int = savedStateHandle["employmentId"] ?: error("Missing employmentId")

    private val _consentState = MutableStateFlow<FeatureState<Consent>>(FeatureState.Loading)
    val consentState: StateFlow<FeatureState<Consent>> = _consentState

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _agreedTerms = MutableStateFlow<Boolean>(false)
    val agreedTerms: StateFlow<Boolean> = _agreedTerms

    init {
        loadConsent()
    }

    fun loadConsent() {
        viewModelScope.launch {
            _consentState.value = FeatureState.Loading

            val consentName = ConsentEnum.EMPLOYMENT_REQUESTS_INITIATION.key
            val response = withVisibleLoading { getConsentsByNameUseCase(consentName) }

            _consentState.value = response
        }
    }

    fun setAgreed(agreed: Boolean) {
        _agreedTerms.value = agreed
    }

    fun respondToRequest(status: EmploymentRequestStatusEnum) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading

            val response = withVisibleLoading { respondEmploymentRequestUseCase(status, employmentId) }

            response
                .onFailure { e ->
                    Timber.tag("Employment Request").e("ERROR: on Responding Employment Request $e")
                    _isSaving.value = FeatureState.Error()
                }
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)
                }
        }
    }
}