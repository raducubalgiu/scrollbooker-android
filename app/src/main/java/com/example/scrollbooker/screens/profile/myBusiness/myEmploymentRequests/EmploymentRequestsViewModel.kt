package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ConsentEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.consent.domain.model.Consent
import com.example.scrollbooker.entity.consent.domain.useCase.GetConsentsByNameUseCase
import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequestCreate
import com.example.scrollbooker.entity.employmentRequest.domain.useCase.CreateEmploymentRequestUseCase
import com.example.scrollbooker.entity.employmentRequest.domain.useCase.GetEmploymentRequestsUseCase
import com.example.scrollbooker.entity.profession.domain.model.Profession
import com.example.scrollbooker.entity.profession.domain.useCase.GetProfessionsByBusinessTypeUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.SearchUsersClientsUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.store.AuthDataStore
import com.google.android.gms.common.Feature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmploymentRequestsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getEmploymentRequestsUseCase: GetEmploymentRequestsUseCase,
    private val searchUsersClientsUseCase: SearchUsersClientsUseCase,
    private val getProfessionsByBusinessTypeUseCase: GetProfessionsByBusinessTypeUseCase,
    private val getConsentsByNameUseCase: GetConsentsByNameUseCase,
    private val createEmploymentRequestUseCase: CreateEmploymentRequestUseCase,
): ViewModel() {
    private val _employmentRequests =
        MutableStateFlow<FeatureState<List<EmploymentRequest>>>(FeatureState.Loading)
    val employmentRequests: StateFlow<FeatureState<List<EmploymentRequest>>> = _employmentRequests

    private val _searchUsersClientsState = MutableStateFlow<FeatureState<List<UserSocial>>?>(null)
    val searchUsersClientsState: StateFlow<FeatureState<List<UserSocial>>?> = _searchUsersClientsState

    private val _selectedEmployee = MutableStateFlow<UserSocial?>(null)
    val selectedEmployee: StateFlow<UserSocial?> = _selectedEmployee

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery

    private val _professionsState = MutableStateFlow<FeatureState<List<Profession>>>(FeatureState.Loading)
    val professionsState: StateFlow<FeatureState<List<Profession>>> = _professionsState

    private val _selectedProfession = MutableStateFlow<Profession?>(null)
    val selectedProfession: StateFlow<Profession?> = _selectedProfession

    private val _consentState = MutableStateFlow<FeatureState<Consent>>(FeatureState.Loading)
    val consentState: StateFlow<FeatureState<Consent>> = _consentState

    private val _agreedConsent = MutableStateFlow<Boolean>(false)
    val agreedConsent: StateFlow<Boolean> = _agreedConsent

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    init {
        loadEmploymentRequests()
        loadProfessions()
        loadConsent()
    }

    fun setSelectedEmployee(employee: UserSocial) {
        _selectedEmployee.value = employee
    }

    private fun loadEmploymentRequests() {
        viewModelScope.launch {
            _employmentRequests.value = FeatureState.Loading
            _employmentRequests.value = withVisibleLoading {
                getEmploymentRequestsUseCase()
            }
        }
    }

    fun setSelectedProfession(profession: Profession) {
        _selectedProfession.value = profession
    }

    fun setAgreedConsent(agreed: Boolean) {
        _agreedConsent.value = agreed
    }

    private fun loadProfessions() {
        viewModelScope.launch {
            _professionsState.value = FeatureState.Loading

            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()

            if(businessTypeId == null) {
                Timber.tag("Professions").e("Business Type Id Not Found")

                _professionsState.value = FeatureState.Error()
            } else {
                _professionsState.value = getProfessionsByBusinessTypeUseCase(businessTypeId)
            }
        }
    }

    private fun loadConsent() {
        viewModelScope.launch {
            _consentState.value = FeatureState.Loading

            val consentName = ConsentEnum.EMPLOYMENT_REQUESTS_INITIATION.key

            _consentState.value = getConsentsByNameUseCase(consentName)
        }
    }

    private var debounceJob: Job? = null

    fun searchEmployees(query: String) {
        _currentQuery.value = query

        if(query.length < 3) {
            debounceJob?.cancel()
            _searchUsersClientsState.value = null
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(200)

            val latest = currentQuery.value
            if(latest.length < 3 || latest != query) return@launch

            _searchUsersClientsState.value = FeatureState.Loading

            _searchUsersClientsState.value = withVisibleLoading {
                searchUsersClientsUseCase(query)
            }
        }
    }

    suspend fun createEmploymentRequest(): Result<Unit> {
        _isSaving.value = true

        val consent = consentState.first { it is FeatureState.Success }
        val consentId = (consent as FeatureState.Success).data.id

        val result = withVisibleLoading {
            createEmploymentRequestUseCase(
                EmploymentRequestCreate(
                    employeeId = _selectedEmployee.value!!.id,
                    professionId = _selectedProfession.value!!.id,
                    consentId = consentId
                )
            )
        }

        result
            .onFailure { e ->
                Timber.tag("Employment Requests").e("ERROR: on Sending Employment Request $e")
            }
            .onSuccess {
                loadEmploymentRequests()
            }

        _isSaving.value = false
        return result
    }
}