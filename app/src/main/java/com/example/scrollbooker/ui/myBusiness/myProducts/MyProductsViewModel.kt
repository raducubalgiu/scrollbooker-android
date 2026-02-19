package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetAllServiceDomainsWithServicesByUserIdUseCase

import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

data class ServicesTabsState(
    val selectedDomainIndex: Int = 0,
    val selectedServicePerDomain: Map<Int, Int> = emptyMap()
)

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getAllServiceDomainsWithServicesByUserIdUseCase: GetAllServiceDomainsWithServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val userIdFlow = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val servicesState: StateFlow<FeatureState<List<ServiceDomainWithEmployeeServices>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getAllServiceDomainsWithServicesByUserIdUseCase(userId, onlyWithProducts = false)
                }

                emit(
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("Service Domains").e(e, "ERROR: on Fetching User ServiceDomain With Services")
                            FeatureState.Error()
                        }
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    // Tabs
    private val _state = MutableStateFlow(ServicesTabsState())
    val state: StateFlow<ServicesTabsState> = _state.asStateFlow()

    fun selectDomain(index: Int) {
        _state.update {
            it.copy(selectedDomainIndex = index)
        }
    }

    fun selectService(domainIndex: Int, serviceIndex: Int) {
        _state.update {
            it.copy(
                selectedServicePerDomain =
                    it.selectedServicePerDomain + (domainIndex to serviceIndex)
            )
        }
    }

    fun getSelectedService(domainIndex: Int): Int {
        return _state.value.selectedServicePerDomain[domainIndex] ?: 0
    }
}