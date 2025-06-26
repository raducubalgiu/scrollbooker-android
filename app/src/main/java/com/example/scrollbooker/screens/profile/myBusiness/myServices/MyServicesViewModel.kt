package com.example.scrollbooker.screens.profile.myBusiness.myServices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.service.domain.model.Service
import com.example.scrollbooker.entity.service.domain.useCase.AttachManyServicesUseCase
import com.example.scrollbooker.entity.service.domain.useCase.DetachServiceUseCase
import com.example.scrollbooker.entity.service.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.entity.service.domain.useCase.GetServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyServicesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getServicesByBusinessIdUseCase: GetServicesByBusinessIdUseCase,
    private val getServicesByBusinessTypeUseCase: GetServicesByBusinessTypeUseCase,
    private val attachManyServicesUseCase: AttachManyServicesUseCase,
    private val detachServiceUseCase: DetachServiceUseCase
): ViewModel() {

    private val _state = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val state: StateFlow<FeatureState<List<Service>>> = _state

    private val _selectedServiceIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedServiceIds: StateFlow<Set<Int>> = _selectedServiceIds.asStateFlow()

    init {
        fetchServices()
    }

    fun fetchServices() {
        viewModelScope.launch {
            _state.value = FeatureState.Loading
            val businessId = authDataStore.getBusinessId().firstOrNull()
            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()

            if(businessId == null) {
                throw IllegalStateException("Business Id not found in data store")
            }

            if(businessTypeId == null) {
                throw IllegalStateException("BusinessType Id not found in data store")
            }

            val user = getServicesByBusinessIdUseCase(businessId)
            val all = getServicesByBusinessTypeUseCase(businessTypeId)

            if(all.isSuccess && user.isSuccess) {
                val selectedIds = user.getOrThrow().map { it.id }.toSet()
                _selectedServiceIds.value = selectedIds

                val combined = all.getOrThrow().map {
                    Service(
                        id = it.id,
                        name = it.name,
                        businessDomainId = it.businessDomainId
                    )
                }

                _state.value = FeatureState.Success(combined)
            } else {
                val error = all.exceptionOrNull() ?: user.exceptionOrNull()
                Timber.tag("Services").e("ERROR: on Fetching Services $error")
                _state.value = FeatureState.Error(error ?: Exception("Unexpected Error"))
            }
        }
    }

    fun toggleService(serviceId: Int) {
        _selectedServiceIds.update { current ->
            if(serviceId in current) current - serviceId else current + serviceId
        }
    }
}