package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.get

data class ServicesTabsState(
    val selectedDomainIndex: Int = 0,
    val selectedServicePerDomain: Map<Int, Int> = emptyMap(),
    val selectedEmployeePerService: Map<String, Int> = emptyMap()
)

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val getAllServiceDomainsWithServicesByUserIdUseCase: GetAllServiceDomainsWithServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val authDataStore: AuthDataStore,
): ViewModel() {
    private val productsCache = mutableMapOf<Triple<Int, Int, Int?>, FeatureState<List<ProductSection>>>()
    private val refreshRequests = MutableSharedFlow<Triple<Int, Int, Int?>>(extraBufferCapacity = 32)

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

    fun getCurrentServiceDomainId(): Int? {
        val serviceDomainState = serviceDomains.value
        if (serviceDomainState is FeatureState.Success) {
            val domains = serviceDomainState.data.serviceDomains
            val selectedDomainIndex = _tabsState.value.selectedDomainIndex

            return if (selectedDomainIndex < domains.size) {
                domains[selectedDomainIndex].id
            } else {
                null
            }
        }
        return null
    }

    fun getCurrentServiceId(): Int? {
        val serviceDomainState = serviceDomains.value
        if (serviceDomainState is FeatureState.Success) {
            val domains = serviceDomainState.data.serviceDomains
            val selectedDomainIndex = _tabsState.value.selectedDomainIndex

            if (selectedDomainIndex < domains.size) {
                val selectedDomain = domains[selectedDomainIndex]
                val selectedServiceIndex = _tabsState.value.selectedServicePerDomain[selectedDomainIndex] ?: 0

                return if (selectedServiceIndex < selectedDomain.services.size) {
                    selectedDomain.services[selectedServiceIndex].id
                } else {
                    null
                }
            }
        }
        return null
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
    val serviceDomains: StateFlow<FeatureState<ServiceDomainWithEmployeeServicesResponse>> = userIdFlow
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
    val productSections: StateFlow<FeatureState<List<ProductSection>>> = run {
        val contextFlow = combine(
            userIdFlow,
            _tabsState,
            serviceDomains
        ) { userId, tabsState, serviceDomainState ->
            val (serviceId, employeeId) = extractServiceAndEmployeeIds(tabsState, serviceDomainState)
            ProductSectionsRequestContext(
                userId = userId,
                serviceId = serviceId,
                employeeId = employeeId,
                serviceDomainState = serviceDomainState,
                forceRefresh = false
            )
        }
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1
            )

        kotlinx.coroutines.flow.merge(
            contextFlow,
            refreshRequests
                .flatMapLatest { refreshKey ->
                    contextFlow
                        .filter { it.cacheKey == refreshKey }
                        .take(1)
                        .map { it.copy(forceRefresh = true) }
                }
        )
            .flatMapLatest { context ->
                flow {
                    val userId = context.userId
                    val serviceId = context.serviceId
                    val employeeId = context.employeeId
                    val forceRefresh = context.forceRefresh

                    if (serviceId == null) {
                        when (context.serviceDomainState) {
                            is FeatureState.Loading -> emit(FeatureState.Loading)
                            is FeatureState.Error -> emit(FeatureState.Error())
                            is FeatureState.Success -> emit(FeatureState.Success(emptyList()))
                        }
                        return@flow
                    }

                    val cacheKey = Triple(userId, serviceId, employeeId)
                    val cachedResult = productsCache[cacheKey]

                    if (cachedResult != null && !forceRefresh) {
                        emit(cachedResult)
                    } else {
                        emit(FeatureState.Loading)

                        val result = withVisibleLoading {
                            getProductsByUserIdAndServiceIdUseCase(
                                userId = userId,
                                serviceId = serviceId,
                                employeeId = employeeId
                            )
                        }

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
    }


    private data class ProductSectionsRequestContext(
        val userId: Int,
        val serviceId: Int?,
        val employeeId: Int?,
        val serviceDomainState: FeatureState<ServiceDomainWithEmployeeServicesResponse>,
        val forceRefresh: Boolean = false
    ) {
        val cacheKey: Triple<Int, Int, Int?>?
            get() = serviceId?.let { Triple(userId, it, employeeId) }
    }

    private fun extractServiceAndEmployeeIds(
        tabsState: ServicesTabsState,
        serviceDomainState: FeatureState<ServiceDomainWithEmployeeServicesResponse>
    ): Pair<Int?, Int?> {
        return if (serviceDomainState is FeatureState.Success) {
            val domains = serviceDomainState.data.serviceDomains
            val selectedDomainIndex = tabsState.selectedDomainIndex

            if (selectedDomainIndex < domains.size) {
                val selectedDomain = domains[selectedDomainIndex]
                val selectedServiceIndex = tabsState.selectedServicePerDomain[selectedDomainIndex] ?: 0

                if (selectedServiceIndex < selectedDomain.services.size) {
                    val service = selectedDomain.services[selectedServiceIndex]

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

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            _isSaving.value = true

            val result = withVisibleLoading {
                deleteProductUseCase(productId)
            }

            result
                .onSuccess {
                    productsCache.clear()
                    Timber.tag("Delete Product").d("Product deleted successfully: $productId")

                    refreshCurrentProductSections()
                    _isSaving.value = false
                }
                .onFailure { e ->
                    Timber.tag("Delete Product").e("ERROR: on Deleting Product $productId - ${e.message}")
                    _isSaving.value = false
                }
        }
    }

    fun refreshCurrentProductSections() {
        val serviceDomainState = serviceDomains.value
        val userId = userIdFlow.replayCache.firstOrNull() ?: return

        val (serviceId, employeeId) = extractServiceAndEmployeeIds(
            tabsState = _tabsState.value,
            serviceDomainState = serviceDomainState
        )

        if (serviceId == null) return

        val key = Triple(userId, serviceId, employeeId)
        productsCache.remove(key)
        refreshRequests.tryEmit(key)
    }
}