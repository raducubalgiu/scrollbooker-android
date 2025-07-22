package com.example.scrollbooker.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
    private val getAllBusinessTypesUseCase: GetAllBusinessTypesUseCase,
    private val getAllBusinessDomainsUseCase: GetAllBusinessDomainsUseCase,
    private val getAllBusinessTypesByBusinessDomainUseCase: GetAllBusinessTypesByBusinessDomainUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val authDataStore: AuthDataStore,
): ViewModel() {
    private val _userProfileState = MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    var appointmentsState by mutableIntStateOf(0)
        private set

    private val _businessTypesState =
        MutableStateFlow<FeatureState<List<BusinessType>>>(FeatureState.Loading)
    val businessTypesState: StateFlow<FeatureState<List<BusinessType>>> = _businessTypesState

    private val _businessTypesByBusinessDomainState =
        MutableStateFlow<Map<Int, FeatureState<List<BusinessType>>>>(emptyMap())
    val businessTypesByBusinessDomainState: StateFlow<Map<Int, FeatureState<List<BusinessType>>>> = _businessTypesByBusinessDomainState

    private val _businessDomainsState =
        MutableStateFlow<FeatureState<List<BusinessDomain>>>(FeatureState.Loading)
    val businessDomainsState: StateFlow<FeatureState<List<BusinessDomain>>> = _businessDomainsState

//    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
//    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    fun increaseAppointmentsNumber() {
        appointmentsState = appointmentsState + 1
    }

    fun decreaseAppointmentsNumber() {
        if(appointmentsState > 0) {
            appointmentsState = appointmentsState - 1
        }
    }

    fun hasBusinessTypesForMain(businessDomainId: Int): Boolean {
        return _businessTypesByBusinessDomainState.value[businessDomainId] != null
    }

    fun fetchBusinessTypesByBusinessDomain(businessDomainId: Int) {
        viewModelScope.launch {
            _businessTypesByBusinessDomainState.update { current ->
                current + (businessDomainId to FeatureState.Loading)
            }

            val result = withVisibleLoading {
                getAllBusinessTypesByBusinessDomainUseCase(businessDomainId)
            }

            _businessTypesByBusinessDomainState.update { current ->
                current + (businessDomainId to result)
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _userProfileState.value = FeatureState.Loading
            val userId = authDataStore.getUserId().firstOrNull()

            val response = getUserProfileUseCase(userId)
            _userProfileState.value = response
        }
    }

    private fun loadAllBusinessTypes() {
        viewModelScope.launch {
            _businessTypesState.value = FeatureState.Loading
            _businessTypesState.value = getAllBusinessTypesUseCase()
        }
    }

    private fun loadAllBusinessDomains() {
        viewModelScope.launch {
            _businessDomainsState.value = FeatureState.Loading
            _businessDomainsState.value = getAllBusinessDomainsUseCase()
        }
    }

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            appointmentsState = getUserAppointmentsNumberUseCase()
        }
    }

    init {
        loadUserProfile()
        loadAppointmentsNumber()
        loadAllBusinessTypes()
        loadAllBusinessDomains()
    }
}