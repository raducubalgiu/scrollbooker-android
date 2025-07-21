package com.example.scrollbooker.screens.profile.myBusiness.myServices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessServicesUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase
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
    private val updateBusinessServicesUseCase: UpdateBusinessServicesUseCase
): ViewModel() {

    private val _state = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val state: StateFlow<FeatureState<List<Service>>> = _state

    private val _defaultSelectedServiceIds = MutableStateFlow<Set<Int>>(emptySet())
    val defaultSelectedServiceIds: StateFlow<Set<Int>> = _defaultSelectedServiceIds.asStateFlow()

    private val _selectedServiceIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedServiceIds: StateFlow<Set<Int>> = _selectedServiceIds.asStateFlow()

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    init {
        fetchServices()
    }

    fun fetchServices() {
        viewModelScope.launch {
            _state.value = FeatureState.Loading

            val result = withVisibleLoading {
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
                    _defaultSelectedServiceIds.value = selectedIds

                    val combined = all.getOrThrow().map {
                        Service(
                            id = it.id,
                            name = it.name,
                            businessDomainId = it.businessDomainId
                        )
                    }

                    return@withVisibleLoading FeatureState.Success(combined)
                } else {
                    val error = all.exceptionOrNull() ?: user.exceptionOrNull()
                    Timber.tag("Services").e("ERROR: on Fetching Services $error")
                    return@withVisibleLoading FeatureState.Error(error ?: Exception("Unexpected Error"))
                }
            }

            _state.value = result
        }
    }

    fun toggleService(serviceId: Int) {
        _selectedServiceIds.update { current ->
            if(serviceId in current) current - serviceId else current + serviceId
        }
    }

    suspend fun updateBusinessServices(): AuthState? {
        _isSaving.value = FeatureState.Loading

        val serviceIds = _selectedServiceIds.value.toList()
        val result = withVisibleLoading { updateBusinessServicesUseCase(serviceIds) }

        return result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update Services").e("ERROR: on updating Business Services $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }
            .getOrNull()
    }
}