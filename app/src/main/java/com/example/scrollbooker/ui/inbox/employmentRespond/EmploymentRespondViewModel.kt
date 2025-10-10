package com.example.scrollbooker.ui.inbox.employmentRespond
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ConsentEnum
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase.RespondEmploymentRequestUseCase
import com.example.scrollbooker.entity.nomenclature.consent.domain.model.Consent
import com.example.scrollbooker.entity.nomenclature.consent.domain.useCase.GetConsentsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmploymentRespondViewModel @Inject constructor(
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
        Timber.tag("EMPLOYMENT REQUEST!!!").e("EMPLOYMENT ID!!! $employmentId")

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