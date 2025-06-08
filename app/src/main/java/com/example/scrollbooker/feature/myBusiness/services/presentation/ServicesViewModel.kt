package com.example.scrollbooker.feature.myBusiness.services.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.services.domain.useCase.AttachManyServicesUseCase
import com.example.scrollbooker.feature.myBusiness.services.domain.useCase.DetachServiceUseCase
import com.example.scrollbooker.feature.myBusiness.services.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.feature.myBusiness.services.domain.useCase.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase,
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
            _servicesState.value = FeatureState.Loading
            _servicesState.value = getServicesUseCase()
        }
    }

    private fun loadAvailableServices() {
        viewModelScope.launch {
            _availableServicesState.value = FeatureState.Loading
            _availableServicesState.value = getServicesByBusinessTypeUseCase()
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