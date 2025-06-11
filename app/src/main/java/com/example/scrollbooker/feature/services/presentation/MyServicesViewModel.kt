package com.example.scrollbooker.feature.services.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.services.domain.model.Service
import com.example.scrollbooker.feature.services.domain.useCase.AttachManyServicesUseCase
import com.example.scrollbooker.feature.services.domain.useCase.DetachServiceUseCase
import com.example.scrollbooker.feature.services.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.feature.services.domain.useCase.GetServicesByUserIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyServicesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getServicesByUserIdUseCase: GetServicesByUserIdUseCase,
    private val getServicesByBusinessTypeUseCase: GetServicesByBusinessTypeUseCase,
    private val attachManyServicesUseCase: AttachManyServicesUseCase,
    private val detachServiceUseCase: DetachServiceUseCase
): ViewModel() {

    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val _availableServicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val availableServicesState: StateFlow<FeatureState<List<Service>>> = _availableServicesState

    private val _actionState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Success(Unit))
    val actionState: StateFlow<FeatureState<Unit>> = _actionState

    init {
        loadServices()
        loadAvailableServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Services").e("ERROR: User Id not found in DataStore")
                _availableServicesState.value = FeatureState.Error()
            } else {
                _servicesState.value = FeatureState.Loading
                _servicesState.value = getServicesByUserIdUseCase(userId)
            }
        }
    }

    private fun loadAvailableServices() {
        viewModelScope.launch {
            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()

            if(businessTypeId == null) {
                Timber.tag("Services").e("ERROR: Business Type Id not found in DataStore")
                _availableServicesState.value = FeatureState.Error()
            } else {
                _availableServicesState.value = FeatureState.Loading
                _availableServicesState.value = getServicesByBusinessTypeUseCase(businessTypeId)
            }
        }
    }

    fun attachManyServices(serviceIds: List<Int>) {
        viewModelScope.launch {
            _actionState.value = FeatureState.Loading
            _actionState.value = attachManyServicesUseCase(serviceIds)

            loadServices()
            loadAvailableServices()
        }
    }

    fun detachServices(serviceId: Int) {
        viewModelScope.launch {
            _actionState.value = FeatureState.Loading
            _actionState.value = detachServiceUseCase(serviceId)

            loadServices()
            loadAvailableServices()
        }
    }
}