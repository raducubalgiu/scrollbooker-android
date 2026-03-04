package com.example.scrollbooker.ui.myBusiness.myProducts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductByIdUseCase
import com.example.scrollbooker.ui.myBusiness.myProducts.components.AddProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class EditProductsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val serviceDomainId: StateFlow<Int?> = savedStateHandle.getStateFlow("serviceDomainId", null)
    private val productId: StateFlow<Int?> = savedStateHandle.getStateFlow("productId", null)

    private val _productState = MutableStateFlow<AddProductState>(
        AddProductState(serviceDomainId = serviceDomainId.value?.toString().orEmpty())
    )
    val productState: StateFlow<AddProductState> = _productState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<SelectedFilters>(emptyMap())
    val selectedFilters: StateFlow<SelectedFilters> = _selectedFilters.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _loadingState = MutableStateFlow<FeatureState<Unit>>(FeatureState.Loading)
    val loadingState: StateFlow<FeatureState<Unit>> = _loadingState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            val id = productId.value ?: return@launch

            _loadingState.value = FeatureState.Loading

            val result = withVisibleLoading { getProductByIdUseCase(id) }

            when (result) {
                is FeatureState.Success -> {
                    val product = result.data

                    _productState.update { current ->
                        current.copy(
                            name = product.name,
                            description = product.description.orEmpty(),
                            price = product.price.toPlainString(),
                            priceWithDiscount = product.priceWithDiscount.toPlainString(),
                            discount = product.discount.toPlainString(),
                            duration = product.duration.toString(),
                            serviceId = product.serviceId.toString(),
                            canBeBooked = product.canBeBooked,
                            type = product.type,
                            sessionsCount = product.sessionsCount?.toString().orEmpty(),
                            validityDays = product.validityDays?.toString().orEmpty()
                        )
                    }

                    // Map product filters to selectedFilters
                    product.filters.forEach { filter ->
                        when {
                            filter.type?.key == "options" && filter.subFilters.isNotEmpty() -> {
                                val subFilterIds = filter.subFilters.map { it.id }.toSet()
                                _selectedFilters.update { current ->
                                    current + (filter.id to FilterSelection.Options(subFilterIds))
                                }
                            }
                            filter.type?.key == "range" -> {
                                _selectedFilters.update { current ->
                                    current + (filter.id to FilterSelection.Range(
                                        minim = filter.minim,
                                        maxim = filter.maxim,
                                        isNotApplicable = false
                                    ))
                                }
                            }
                        }
                    }

                    _loadingState.value = FeatureState.Success(Unit)
                }

                is FeatureState.Error -> {
                    Timber.tag("EditProduct").e("ERROR: on Loading Product by Id")
                    _loadingState.value = FeatureState.Error()
                }
                else -> Unit
            }
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

    fun setPrice(price: String) {
        _productState.update { current -> current.copy(price = price) }
    }

    fun setDiscount(discount: String) {
        _productState.update { current -> current.copy(discount = discount) }
    }

    fun setServiceDomainId(domainId: String) {
        _productState.update { current -> current.copy(serviceDomainId = domainId) }
    }

    fun setServiceId(serviceId: String) {
        _productState.update { current -> current.copy(serviceId = serviceId) }
    }

    fun setCanBeBooked(can: Boolean) {
        _productState.update { current -> current.copy(canBeBooked = can) }
    }

    fun editProduct() {

    }
}