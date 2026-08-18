package com.example.scrollbooker.ui.myBusiness.myProducts.productState

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.data.remote.ProductBaseInfoUpdateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductByIdUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.UpdateProductBaseInfoUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.ProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductBaseInfoUseCase: UpdateProductBaseInfoUseCase,
    getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    authDataStore: AuthDataStore,
    savedStateHandle: SavedStateHandle
) : ProductViewModel(
    context,
    getAllEmployeesByOwnerUseCase,
    getFiltersByServiceUseCase,
    getSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    authDataStore
) {
    private val productId: StateFlow<Int?> = savedStateHandle.getStateFlow("productId", null)

    private val _loadingProductState = MutableStateFlow<FeatureState<Product>>(FeatureState.Loading)
    val loadingProductState: StateFlow<FeatureState<Product>> = _loadingProductState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            val id = productId.value ?: return@launch
            _loadingProductState.value = FeatureState.Loading

            val result = withVisibleLoading { getProductByIdUseCase(id) }

            result.fold(
                onSuccess = { product ->
                    _currentServiceId.value = product.serviceId

                    _productState.update {
                        it.copy(
                            name = product.name,
                            description = product.description.orEmpty(),
                            serviceDomainId = product.serviceDomainId.toString(),
                            serviceId = product.serviceId.toString(),
                            currencyId = product.currencyId.toString(),
                            variants = product.variants.map { variant ->
                                ProductVariantState(
                                    name = variant.name,
                                    duration = variant.duration.toString(),
                                    offerings = variant.offerings.map { offering ->
                                        ProductOfferingState(
                                            userId = offering.user.id.toString(),
                                            price = offering.price.toPlainString(),
                                            discount = offering.discount.toPlainString(),
                                            priceWithDiscount = offering.priceWithDiscount.toPlainString(),
                                            isSelected = true
                                        )
                                    }
                                )
                            }
                        )
                    }

                    product.filters.forEach { filter ->
                        _selectedFilters.update {
                            it + (filter.id to filter.subFilters.map { it.id }.toSet())
                        }
                    }

                    _loadingProductState.value = FeatureState.Success(product)
                },
                onFailure = { error ->
                    Timber.Forest.tag("Edit Product").e(error, "ERROR: on Fetching Product with id: $id")
                    _loadingProductState.value = FeatureState.Error(error)
                }
            )
        }
    }

    fun editProductBaseInfo() {
        viewModelScope.launch {
            val id = productId.value ?: return@launch

            _isSaving.value = true

            val filters: List<ProductFilterRequest> =
                _selectedFilters.value.entries.mapNotNull { (filterId, subFilterIdsSet) ->
                    if (subFilterIdsSet.isEmpty()) return@mapNotNull null

                    ProductFilterRequest(
                        filterId = filterId,
                        subFilterIds = subFilterIdsSet.toList(),
                        isNotApplicable = false
                    )
                }

            val request = ProductBaseInfoUpdateRequest(
                name = _productState.value.name,
                description = _productState.value.description,
                serviceDomainId = _productState.value.serviceDomainId.toInt(),
                serviceId = _productState.value.serviceId.toInt(),
                filters = filters
            )

            val result = withVisibleLoading {
                updateProductBaseInfoUseCase(productId = id, request = request)
            }

            result.fold(
                onSuccess = {
                    _isSaving.value = false
                    _createSuccessEvent.tryEmit(Unit)
                },
                onFailure = { error ->
                    Timber.Forest.tag("Edit Product").e(error, "ERROR: on Updating Product with id: $id")
                    _isSaving.value = false
                }
            )
        }
    }

}