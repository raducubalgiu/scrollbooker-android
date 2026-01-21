package com.example.scrollbooker.ui.myBusiness.myEmploymentRequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ConsentEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestCreate
import com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase.CreateEmploymentRequestUseCase
import com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase.GetEmploymentRequestsUseCase
import com.example.scrollbooker.entity.nomenclature.consent.domain.model.Consent
import com.example.scrollbooker.entity.nomenclature.consent.domain.useCase.GetConsentsByNameUseCase
import com.example.scrollbooker.entity.nomenclature.profession.domain.model.Profession
import com.example.scrollbooker.entity.nomenclature.profession.domain.useCase.GetProfessionsByBusinessTypeUseCase
import com.example.scrollbooker.entity.search.domain.useCase.SearchUsersUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class EmploymentRequestsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getEmploymentRequestsUseCase: GetEmploymentRequestsUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
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

    init {
        _currentQuery
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(200)
            .onEach { query ->
                if (query.length < 2) {
                    _searchUsersClientsState.value = null
                    return@onEach
                }

                _searchUsersClientsState.value = FeatureState.Loading
            }
            .filter { it.length >= 2 }
            .mapLatest { query ->
                withVisibleLoading { searchUsersUseCase(query) }
            }
            .onEach { result -> _searchUsersClientsState.value = result }
            .catch { e -> _searchUsersClientsState.value = FeatureState.Error(e) }
            .launchIn(viewModelScope)
    }

    fun handleSearch(query: String) {
        _currentQuery.value = query
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