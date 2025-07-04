package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.consent.domain.model.Consent
import com.example.scrollbooker.entity.consent.domain.useCase.GetConsentsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmploymentAcceptTermsViewModel @Inject constructor(
    private val getConsentsByNameUseCase: GetConsentsByNameUseCase
): ViewModel() {
    private val _consentState = MutableStateFlow<FeatureState<Consent>>(FeatureState.Loading)
    val consentState: StateFlow<FeatureState<Consent>> = _consentState

    init {
        loadConsent()
    }

    fun loadConsent() {
        viewModelScope.launch {
            _consentState.value = FeatureState.Loading

            val consentName = "EMPLOYMENT_REQUESTS_CONSENT"

            _consentState.value = getConsentsByNameUseCase(consentName)
        }
    }
}