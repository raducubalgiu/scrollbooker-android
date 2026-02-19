package com.example.scrollbooker.ui.myBusiness.myServices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessServicesUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.asSequence

@HiltViewModel
class MyServicesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    private val updateBusinessServicesUseCase: UpdateBusinessServicesUseCase
): ViewModel() {
    private val _state = MutableStateFlow<FeatureState<List<SelectedServiceDomainsWithServices>>>(FeatureState.Loading)
    val state: StateFlow<FeatureState<List<SelectedServiceDomainsWithServices>>> = _state

    private val _defaultSelectedServiceIds = MutableStateFlow<Set<Int>>(emptySet())
    val defaultSelectedServiceIds: StateFlow<Set<Int>> = _defaultSelectedServiceIds.asStateFlow()

    private val _selectedServiceIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedServiceIds: StateFlow<Set<Int>> = _selectedServiceIds.asStateFlow()

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    init {
        fetchServices()
    }

    fun fetchServices() {
        viewModelScope.launch {
            _state.value = FeatureState.Loading

            val businessId = authDataStore.getBusinessId().firstOrNull()

            if(businessId == null) {
                throw IllegalStateException("Business Id not found in data store")
            }

            val result = withVisibleLoading {
                getSelectedServiceDomainsWithServicesByBusinessIdUseCase(businessId)
            }

            if (result is FeatureState.Success) {
                val data = result.data

                _defaultSelectedServiceIds.value = data
                    .asSequence()
                    .flatMap { it.services.asSequence() }
                    .filter { it.isSelected }
                    .map { it.id }
                    .toSet()

                _selectedServiceIds.value = data
                    .asSequence()
                    .flatMap { it.services.asSequence() }
                    .filter { it.isSelected }
                    .map { it.id }
                    .toSet()
            }

            _state.value = result
        }
    }

    fun toggleService(serviceId: Int) {
        _selectedServiceIds.update { current ->
            if(serviceId in current) current - serviceId else current + serviceId
        }
    }

    suspend fun updateBusinessServices(): List<SelectedServiceDomainsWithServices>? {
        _isSaving.value = FeatureState.Loading

        val serviceIds = _selectedServiceIds.value.toList()
        val result = withVisibleLoading { updateBusinessServicesUseCase(serviceIds) }

        return result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                Timber.tag("Update Services").e("ERROR: on updating Business Services $e")
            }
            .onSuccess { updated ->
                _isSaving.value = FeatureState.Success(Unit)

                _defaultSelectedServiceIds.value = updated
                    .asSequence()
                    .flatMap { it.services.asSequence() }
                    .filter { it.isSelected }
                    .map { it.id }
                    .toSet()

                _selectedServiceIds.value = updated
                    .asSequence()
                    .flatMap { it.services.asSequence() }
                    .filter { it.isSelected }
                    .map { it.id }
                    .toSet()

                _events.tryEmit(
                    SnackBarUiEvent.Show(
                        message = UiText.Resource(R.string.scheduleSaved)
                    )
                )
            }
            .getOrNull()
    }
}