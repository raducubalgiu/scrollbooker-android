package com.example.scrollbooker.ui.shared.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetAllServiceDomainsWithServicesByUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserProductsViewModel @Inject constructor(
    private val getAllServiceDomainsWithServicesByUserIdUseCase: GetAllServiceDomainsWithServicesByUserIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase
): ViewModel() {
    private val userIdFlow = MutableStateFlow<Int?>(null)
    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    private val _selectedProducts = MutableStateFlow<Set<Product>>(emptySet())
    val selectedProducts: StateFlow<Set<Product>> = _selectedProducts.asStateFlow()

    fun setUserId(userId: Int) {
        if(userIdFlow.value != userId) {
            userIdFlow.value = userId
        }
    }

    fun clearProducts() {
        _selectedProducts.value = emptySet()
    }

    fun setMultipleProducts(products: List<Product>) {
        _selectedProducts.value = products.toSet()
    }

    fun toggleProductId(product: Product) {
        val productsSet = _selectedProducts.value.toMutableSet()

        if(product in productsSet) {
            productsSet.remove(product)
        } else {
            productsSet.add(product)
        }

        _selectedProducts.value = productsSet
    }

    fun reset() {
        userIdFlow.value = null
        _selectedProducts.value = emptySet()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val serviceDomainWithServicesState: StateFlow<FeatureState<List<ServiceDomainWithEmployeeServices>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = getAllServiceDomainsWithServicesByUserIdUseCase(userId)

                emit(
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("Services").e(e, "ERROR: on Fetching User ServiceDomain With Services")
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadProducts(serviceId: Int, userId: Int, employeeId: Int?): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            getProductsByUserIdAndServiceIdUseCase(userId, serviceId, employeeId)
                .cachedIn(viewModelScope)
                .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }
}