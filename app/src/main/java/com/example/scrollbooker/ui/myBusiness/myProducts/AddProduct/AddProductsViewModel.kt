package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.FilterSelection
import com.example.scrollbooker.ui.myBusiness.myProducts.SelectedFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    private val createProductUseCase: CreateProductUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _productState = MutableStateFlow<ProductState>(ProductState())
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<SelectedFilters>(emptyMap())
    val selectedFilters: StateFlow<SelectedFilters> = _selectedFilters.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _createSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val createSuccessEvent = _createSuccessEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees = authDataStore.getBusinessOwnerId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { ownerId ->
            flow {
                emit(FeatureState.Loading)
                try {
                    val employeeList = withVisibleLoading { getAllEmployeesByOwnerUseCase(ownerId) }
                    emit(FeatureState.Success(employeeList))
                } catch (e: Exception) {
                    Timber.tag("Employees").e(e, "ERROR: on Fetching employees failed")
                    emit(FeatureState.Error(e))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    fun setSingleOption(filterId: Int, subFilterId: Int?) {
        _selectedFilters.update { current ->
            if (subFilterId == null) current - filterId
            else current + (filterId to FilterSelection.Options(setOf(subFilterId)))
        }
    }

    fun toggleMultiOption(filterId: Int, subFilterId: Int) {
        _selectedFilters.update { current ->
            val prev = (current[filterId] as? FilterSelection.Options)?.ids.orEmpty()
            val next = if (subFilterId in prev) prev - subFilterId else prev + subFilterId
            if (next.isEmpty()) current - filterId
            else current + (filterId to FilterSelection.Options(next))
        }
    }

    fun setRange(filterId: Int, from: BigDecimal?, to: BigDecimal?) {
        _selectedFilters.update { current ->
            if (from == null && to == null) current - filterId
            else current + (filterId to FilterSelection.Range(minim = from, maxim = to))
        }
    }

    fun setApplicable(filterId: Int) {
        _selectedFilters.update { current ->
            val prev = current[filterId] as? FilterSelection.Range

            val next = if (prev == null) {
                FilterSelection.Range(
                    minim = null,
                    maxim = null,
                    isNotApplicable = true
                )
            } else {
                val newIsNotApplicable = !prev.isNotApplicable

                if (newIsNotApplicable) {
                    prev.copy(
                        minim = null,
                        maxim = null,
                        isNotApplicable = true
                    )
                } else {
                    prev.copy(isNotApplicable = false)
                }
            }

            current + (filterId to next)
        }
    }

    fun setName(name: String) {
        _productState.update { current -> current.copy(name = name) }
    }

    fun setType(type: ProductTypeEnum) {
        _productState.update { current -> current.copy(type = type) }
    }

    fun setSessionsCount(sessionsCount: String) {
        _productState.update { current -> current.copy(sessionsCount = sessionsCount) }
    }

    fun setValidityDays(validityDays: String) {
        _productState.update { current -> current.copy(validityDays = validityDays) }
    }

    fun setDescription(description: String) {
        _productState.update { current -> current.copy(description = description) }
    }

    fun setDuration(duration: String) {
        _productState.update { current -> current.copy(duration = duration) }
    }

//    fun setPrice(price: String) {
//        _productState.update { current -> current.copy(price = price) }
//    }
//
//    fun setDiscount(discount: String) {
//        _productState.update { current -> current.copy(discount = discount) }
//    }

    fun setServiceDomainId(domainId: String) {
        _productState.update { current -> current.copy(serviceDomainId = domainId) }
    }

    fun setServiceId(serviceId: String) {
        _productState.update { current -> current.copy(serviceId = serviceId) }
    }

    fun setCanBeBooked(can: Boolean) {
        _productState.update { current -> current.copy(canBeBooked = can) }
    }

    fun createProduct() {
//        viewModelScope.launch {
//            _isSaving.value = true
//
//            val state = _productState.value
//            val businessId = authDataStore.getBusinessId().firstOrNull()
//
//            if(businessId == null) {
//                throw IllegalStateException("Business Id not found in Auth Data Store")
//            }
//
//            val priceWithDiscount = calculatePriceWithDiscount(state.price, state.discount)
//                .toPlainString()
//
//            val filters: List<AddProductFilterRequest> =
//                _selectedFilters.value.entries.mapNotNull { (filterId, selection) ->
//                    when (selection) {
//                        is FilterSelection.Options -> {
//                            if (selection.ids.isEmpty()) return@mapNotNull null
//
//                            AddProductFilterRequest(
//                                filterId = filterId,
//                                subFilterIds = selection.ids.toList(),
//                                type = FilterTypeEnum.OPTIONS.key,
//                                minim = null,
//                                maxim = null,
//                                isNotApplicable = false
//                            )
//                        }
//
//                        is FilterSelection.Range -> {
//                            if (selection.minim == null && selection.maxim == null) return@mapNotNull null
//
//                            AddProductFilterRequest(
//                                filterId = filterId,
//                                subFilterIds = emptyList(),
//                                type = FilterTypeEnum.RANGE.key,
//                                minim = selection.minim,
//                                maxim = selection.maxim,
//                                isNotApplicable = selection.isNotApplicable
//                            )
//                        }
//                    }
//                }
//
//            val productType = state.type?.key ?: return@launch
//
//            val response = withVisibleLoading {
//                createProductUseCase(
//                    productCreate = ProductCreate(
//                        name = state.name,
//                        description = state.description,
//                        price = state.price.toBigDecimal(),
//                        priceWithDiscount = priceWithDiscount.toBigDecimal(),
//                        discount = state.discount.toBigDecimal(),
//                        duration = state.duration.toInt(),
//                        serviceId = state.serviceId.toInt(),
//                        businessId = businessId,
//                        currencyId = 1,
//                        type = productType,
//                        sessionsCount = state.sessionsCount.toIntOrNull(),
//                        validityDays = state.validityDays.toIntOrNull(),
//                        canBeBooked = state.canBeBooked
//                    ),
//                    serviceDomainId = state.serviceDomainId.toInt(),
//                    filters = filters
//                )
//            }
//
//            response
//                .onSuccess { response ->
//                    _isSaving.value = false
//                    _createSuccessEvent.tryEmit(Unit)
//                }
//                .onFailure { e ->
//                    Timber.Forest.tag("Create Product").e(e, "ERROR: on Creating Product")
//                    _isSaving.value = false
//                }
//        }
    }
}