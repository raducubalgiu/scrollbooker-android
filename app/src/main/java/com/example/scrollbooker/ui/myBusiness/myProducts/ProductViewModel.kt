package com.example.scrollbooker.ui.myBusiness.myProducts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.AddProductValidation
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductState
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber

@Suppress("PropertyName")
abstract class ProductViewModel(
    @ApplicationContext protected val context: Context,
    private val getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    private val getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    private val getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    protected val authDataStore: AuthDataStore
) : ViewModel() {

    val hasEmployees: StateFlow<Boolean?> = authDataStore.getHasEmployees()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val ownerId: StateFlow<Int?> = authDataStore.getBusinessOwnerId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    protected val _productState = MutableStateFlow(ProductState())
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    protected val _currentServiceId = MutableStateFlow<Int?>(null)

    protected val _selectedFilters = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())
    val selectedFilters: StateFlow<Map<Int, Set<Int>>> = _selectedFilters.asStateFlow()

    protected val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    protected val _createSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val createSuccessEvent = _createSuccessEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedServices = authDataStore.getBusinessId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { businessId ->
            flow {
                emit(FeatureState.Loading)
                val result = getSelectedServiceDomainsWithServicesByBusinessIdUseCase(businessId)
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: StateFlow<FeatureState<List<Employee>>> = combine(
        authDataStore.getBusinessOwnerId().filterNotNull(),
        authDataStore.getHasEmployees().filterNotNull()
    ) { ownerId, hasEmployees -> ownerId to hasEmployees }
        .distinctUntilChanged()
        .flatMapLatest { (ownerId, hasEmployees) ->
            if (!hasEmployees) {
                flowOf(FeatureState.Success(emptyList()))
            } else {
                flow {
                    emit(FeatureState.Loading)
                    try {
                        val employeeList = withVisibleLoading {
                            getAllEmployeesByOwnerUseCase(ownerId)
                        }
                        emit(FeatureState.Success(employeeList))
                    } catch (e: Exception) {
                        Timber.tag("Employees").e(e, "ERROR: on Fetching employees failed")
                        emit(FeatureState.Error(e))
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val filters: StateFlow<FeatureState<List<Filter>>> = _currentServiceId
        .flatMapLatest { serviceId ->
            flow {
                if (serviceId == null) {
                    emit(FeatureState.Success(emptyList()))
                    return@flow
                }
                emit(FeatureState.Loading)
                val result = getFiltersByServiceUseCase(serviceId)
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Success(emptyList())
        )

    val productValidation: StateFlow<AddProductValidation> = combine(
        _productState,
        filters,
        _selectedFilters
    ) { state, filtersState, selectedFilters ->
        val filtersList = (filtersState as? FeatureState.Success)?.data.orEmpty()
        state.validate(context, filtersList, selectedFilters)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddProductValidation())

    fun setName(name: String) {
        _productState.update { current -> current.copy(name = name) }
    }

    fun setDescription(description: String) {
        _productState.update { current -> current.copy(description = description) }
    }

    fun setServiceDomainId(domainId: String) {
        _productState.update { current -> current.copy(serviceDomainId = domainId) }
    }

    fun setServiceId(serviceId: String) {
        _productState.update { current -> current.copy(serviceId = serviceId) }
        _selectedFilters.value = emptyMap()
        _currentServiceId.value = serviceId.toIntOrNull()
    }

    fun setFilterOptions(filterId: Int, subFilterIds: Set<Int>) {
        _selectedFilters.update { current ->
            if (subFilterIds.isEmpty()) {
                current - filterId
            } else {
                current + (filterId to subFilterIds)
            }
        }
    }

    fun addVariant(variant: ProductVariantState) {
        _productState.update { current ->
            current.copy(variants = current.variants + variant)
        }
    }

    fun removeVariant(index: Int) {
        _productState.update { current ->
            current.copy(variants = current.variants.filterIndexed { i, _ -> i != index })
        }
    }
}