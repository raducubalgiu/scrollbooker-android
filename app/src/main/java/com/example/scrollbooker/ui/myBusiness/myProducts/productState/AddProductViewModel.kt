package com.example.scrollbooker.ui.myBusiness.myProducts.productState

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductOfferingRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductVariantRequest
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.ProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    @ApplicationContext context: Context,
    getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    authDataStore: AuthDataStore,
    private val createProductUseCase: CreateProductUseCase
) : ProductViewModel(
    context,
    getAllEmployeesByOwnerUseCase,
    getFiltersByServiceUseCase,
    getSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    authDataStore
) {
    fun createProduct() {
        viewModelScope.launch {
            val state = _productState.value
            val globalValidation = productValidation.value

            if (!globalValidation.isValid) {
                Timber.Forest.tag("Create Product").w("Formularul conține erori de validare.")
                return@launch
            }

            _isSaving.value = true

            try {
                val businessId = authDataStore.getBusinessId().firstOrNull()
                val currentUserId = authDataStore.getUserId().firstOrNull()

                if (businessId == null || currentUserId == null) {
                    throw IllegalStateException("Business Id or User Id not found in Auth Data Store")
                }

                val filters: List<ProductFilterRequest> =
                    _selectedFilters.value.entries.mapNotNull { (filterId, subFilterIdsSet) ->
                        if (subFilterIdsSet.isEmpty()) return@mapNotNull null

                        ProductFilterRequest(
                            filterId = filterId,
                            subFilterIds = subFilterIdsSet.toList(),
                            isNotApplicable = false
                        )
                    }

                val productCreateRequest = ProductCreateRequest(
                    name = state.name,
                    description = state.description.ifBlank { null },
                    serviceDomainId = state.serviceDomainId.toIntOrNull() ?: 0,
                    serviceId = state.serviceId.toIntOrNull() ?: 0,
                    businessId = businessId,
                    currencyId = state.currencyId.toIntOrNull() ?: 0,
                    variants = state.variants.map { variantState ->
                        ProductVariantRequest(
                            name = variantState.name,
                            duration = variantState.duration.toIntOrNull() ?: 0,
                            offerings = variantState.offerings
                                .filter { it.isSelected }
                                .distinctBy { it.userId }
                                .map { offeringState ->
                                    ProductOfferingRequest(
                                        userId = offeringState.userId.toInt(),
                                        price = offeringState.price.toBigDecimalOrNull()
                                            ?: BigDecimal.ZERO,
                                        discount = offeringState.discount.toBigDecimalOrNull()
                                            ?: BigDecimal.ZERO,
                                        priceWithDiscount = offeringState.priceWithDiscount.toBigDecimalOrNull()
                                            ?: BigDecimal.ZERO
                                    )
                                }
                        )
                    }
                )

                val response = withVisibleLoading {
                    createProductUseCase(productCreateRequest, filters)
                }

                response.fold(
                    onSuccess = {
                        _isSaving.value = false
                        _createSuccessEvent.tryEmit(Unit)
                    },
                    onFailure = { e ->
                        Timber.Forest.tag("Create Product").e(e, "ERROR: on Creating Product")
                        _isSaving.value = false
                    }
                )

            } catch (e: Exception) {
                Timber.Forest.tag("Create Product").e(e, "ERROR: Unexpected exception during assembly")
                _isSaving.value = false
            }
        }
    }
}