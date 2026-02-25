package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

data class ServicesTabsState(
    val selectedDomainIndex: Int = 0,
    val selectedServicePerDomain: Map<Int, Int> = emptyMap(),
    val selectedEmployeePerService: Map<String, Int> = emptyMap() // Key format: "domainIndex-serviceIndex"
)

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getAllServiceDomainsWithServicesByUserIdUseCase: GetAllServiceDomainsWithServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
): ViewModel() {
    // Cache pentru produse: (userId, serviceId, employeeId) -> FeatureState
    private val productsCache = mutableMapOf<Triple<Int, Int, Int?>, FeatureState<List<ProductSection>>>()

    // Tabs
    private val _tabsState = MutableStateFlow(ServicesTabsState())
    val tabsState: StateFlow<ServicesTabsState> = _tabsState.asStateFlow()

    fun selectDomain(index: Int) {
        _tabsState.update {
            it.copy(selectedDomainIndex = index)
        }
    }

    fun selectService(domainIndex: Int, serviceIndex: Int) {
        _tabsState.update {
            it.copy(
                selectedServicePerDomain =
                    it.selectedServicePerDomain + (domainIndex to serviceIndex)
            )
        }
    }

    fun selectEmployee(domainIndex: Int, serviceIndex: Int, employeeId: Int) {
        val key = "$domainIndex-$serviceIndex"
        _tabsState.update {
            it.copy(
                selectedEmployeePerService =
                    it.selectedEmployeePerService + (key to employeeId)
            )
        }
    }

    fun getSelectedService(domainIndex: Int): Int {
        return _tabsState.value.selectedServicePerDomain[domainIndex] ?: 0
    }

    fun getSelectedEmployee(domainIndex: Int, serviceIndex: Int): Int? {
        val key = "$domainIndex-$serviceIndex"
        return _tabsState.value.selectedEmployeePerService[key]
    }

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val userIdFlow = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val serviceDomains: StateFlow<FeatureState<List<ServiceDomainWithEmployeeServices>>> = userIdFlow
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
            started = SharingStarted.Eagerly,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val productSections: StateFlow<FeatureState<List<ProductSection>>> = combine(
        userIdFlow,
        _tabsState,
        serviceDomains
    ) { userId, tabsState, serviceDomainState ->
        // Extract current serviceId and employeeId based on tab selection
        val (serviceId, employeeId) = extractServiceAndEmployeeIds(tabsState, serviceDomainState)
        Triple(userId, serviceId, employeeId)
    }
        .distinctUntilChanged()
        .flatMapLatest { (userId, serviceId, employeeId) ->
            flow {
                // Only proceed if we have a valid serviceId
                if (serviceId == null) {
                    emit(FeatureState.Success(emptyList()))
                    return@flow
                }

                // Check if we have cached data for this combination
                val cacheKey = Triple(userId, serviceId, employeeId)
                val cachedResult = productsCache[cacheKey]

                if (cachedResult != null) {
                    Timber.tag("ProductSections").d("Using cached data for userId=$userId, serviceId=$serviceId, employeeId=$employeeId")
                    emit(cachedResult)
                } else {
                    // No cache, fetch from API
                    emit(FeatureState.Loading)

                    val result = withVisibleLoading {
                        getProductsByUserIdAndServiceIdUseCase(
                            userId = userId,
                            serviceId = serviceId,
                            employeeId = employeeId
                        )
                    }

                    Timber.tag("ProductSections").d("Fetched products for userId=$userId, serviceId=$serviceId, employeeId=$employeeId")

                    // Store in cache
                    if (result is FeatureState.Success) {
                        productsCache[cacheKey] = result
                    }

                    emit(result)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FeatureState.Loading
        )

    private fun extractServiceAndEmployeeIds(
        tabsState: ServicesTabsState,
        serviceDomainState: FeatureState<List<ServiceDomainWithEmployeeServices>>
    ): Pair<Int?, Int?> {
        return if (serviceDomainState is FeatureState.Success) {
            val domains = serviceDomainState.data
            val selectedDomainIndex = tabsState.selectedDomainIndex

            if (selectedDomainIndex < domains.size) {
                val selectedDomain = domains[selectedDomainIndex]
                val selectedServiceIndex = tabsState.selectedServicePerDomain[selectedDomainIndex] ?: 0

                if (selectedServiceIndex < selectedDomain.services.size) {
                    val service = selectedDomain.services[selectedServiceIndex]

                    // Get selected employeeId for this service, or use first employee if available
                    val key = "$selectedDomainIndex-$selectedServiceIndex"
                    val employeeId = tabsState.selectedEmployeePerService[key]
                        ?: service.employees.firstOrNull()?.id

                    Pair(service.id, employeeId)
                } else {
                    Pair(null, null)
                }
            } else {
                Pair(null, null)
            }
        } else {
            Pair(null, null)
        }
    }
}