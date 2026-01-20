package com.example.scrollbooker.ui.myBusiness.myProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.data.remote.AddProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.components.calculatePriceWithDiscount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.collections.plus

sealed interface FilterSelection {
    data class Options(val ids: Set<Int> = emptySet()): FilterSelection
    data class Range(
        val minim: BigDecimal?,
        val maxim: BigDecimal?,
        val isNotApplicable: Boolean = false
    ): FilterSelection
}

typealias SelectedFilters = Map<Int, FilterSelection>

data class AddProductState(
    val name: String = "",
    val description: String = "",
    val price: String = "0",
    val priceWithDiscount: String = "0",
    val discount: String = "0",
    val duration: String = "",
    val serviceId: String = "",
    val currencyId: String = "",
    val canBeBooked: Boolean = true
)

@HiltViewModel
class AddProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getServicesByBusinessIdUseCase: GetServicesByBusinessIdUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase,
    private val getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    private val createProductUseCase: CreateProductUseCase,
): ViewModel() {
    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val _currenciesState = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val currenciesState: StateFlow<FeatureState<List<Currency>>> = _currenciesState

    private val _filtersState = MutableStateFlow<FeatureState<List<Filter>>?>(null)
    val filtersState: StateFlow<FeatureState<List<Filter>>?> = _filtersState

    private val _productState = MutableStateFlow<AddProductState>(AddProductState())
    val productState: StateFlow<AddProductState> = _productState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<SelectedFilters>(emptyMap())
    val selectedFilters: StateFlow<SelectedFilters> = _selectedFilters.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    init {
        loadServices()
        loadCurrencies()
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _currenciesState.value = FeatureState.Loading

            val result = withVisibleLoading {
                val userId = authDataStore.getUserId().firstOrNull()
                    ?: throw IllegalStateException("User Id not found in authDataStore")

                getUserCurrenciesUseCase(userId)
            }

            result
                .onSuccess { response ->
                    _currenciesState.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Currencies").e("ERROR: on Fetching Currencies in MyProducts $e")
                    _currenciesState.value = FeatureState.Error()
                }
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = FeatureState.Loading

            val result = withVisibleLoading {
                val businessId = authDataStore.getBusinessId().firstOrNull()
                    ?: throw IllegalStateException("Business Id not found in authDataStore")

                getServicesByBusinessIdUseCase(businessId)
            }

            result
                .onSuccess { response ->
                    _servicesState.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Services").e("ERROR: on Fetching Services in MyProducts $e")
                    _servicesState.value = FeatureState.Error()
                }
        }
    }

    fun loadFilters(serviceId: Int) {
        viewModelScope.launch {
            _filtersState.value = FeatureState.Loading
            _filtersState.value = withVisibleLoading { getFiltersByServiceUseCase(serviceId) }
        }
    }

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

    fun resetFilters() {
        _selectedFilters.value = emptyMap()
    }

    fun setName(name: String) {
        _productState.update { current -> current.copy(name = name) }
    }

    fun setDescription(description: String) {
        _productState.update { current -> current.copy(description = description) }
    }

    fun setDuration(duration: String) {
        _productState.update { current -> current.copy(duration = duration) }
    }

    fun setPrice(price: String) {
        _productState.update { current -> current.copy(price = price) }
    }

    fun setDiscount(discount: String) {
        _productState.update { current -> current.copy(discount = discount) }
    }

    fun setCurrencyId(currencyId: String) {
        _productState.update { current -> current.copy(currencyId = currencyId) }
    }

    fun setServiceId(serviceId: String) {
        _productState.update { current -> current.copy(serviceId = serviceId) }
    }

    fun setCanBeBooked(can: Boolean) {
        _productState.update { current -> current.copy(canBeBooked = can) }
    }

    fun createProduct() {
        viewModelScope.launch {
            _isSaving.value = true

            val state = _productState.value
            val businessId = authDataStore.getBusinessId().firstOrNull()

            if(businessId == null) {
                throw kotlin.IllegalStateException("Business Id not found in Auth Data Store")
            }

            val priceWithDiscount = calculatePriceWithDiscount(state.price, state.discount)
                .toPlainString()

            val filters: List<AddProductFilterRequest> =
                _selectedFilters.value.entries.mapNotNull { (filterId, selection) ->
                    when (selection) {
                        is FilterSelection.Options -> {
                            if (selection.ids.isEmpty()) return@mapNotNull null

                            AddProductFilterRequest(
                                filterId = filterId,
                                subFilterIds = selection.ids.toList(),
                                type = FilterTypeEnum.OPTIONS.key,
                                minim = null,
                                maxim = null,
                                isNotApplicable = false
                            )
                        }

                        is FilterSelection.Range -> {
                            if (selection.minim == null && selection.maxim == null) return@mapNotNull null

                            AddProductFilterRequest(
                                filterId = filterId,
                                subFilterIds = emptyList(),
                                type = FilterTypeEnum.RANGE.key,
                                minim = selection.minim,
                                maxim = selection.maxim,
                                isNotApplicable = selection.isNotApplicable
                            )
                        }
                    }
                }

            val response = withVisibleLoading {
                createProductUseCase(
                    productCreate = ProductCreate(
                        name = state.name,
                        description = state.description,
                        price = state.price.toBigDecimal(),
                        priceWithDiscount = priceWithDiscount.toBigDecimal(),
                        discount = state.discount.toBigDecimal(),
                        duration = state.duration.toInt(),
                        serviceId = state.serviceId.toInt(),
                        businessId = businessId,
                        currencyId = state.currencyId.toInt(),
                        canBeBooked = state.canBeBooked
                    ),
                    filters = filters
                )
            }

            response
                .onSuccess { response ->
                    _isSaving.value = false
                }
                .onFailure { e ->
                    Timber.tag("Create Product").e("ERROR: on Creating Product in MyProducts $e")
                    _isSaving.value = false
                }
        }
    }
}