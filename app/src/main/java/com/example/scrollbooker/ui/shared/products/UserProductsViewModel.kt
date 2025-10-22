package com.example.scrollbooker.ui.shared.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByUserIdUseCase
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
    private val getServicesByUserIdUseCase: GetServicesByUserIdUseCase,
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

    fun toggleProductId(product: Product) {
        val current = _selectedProducts.value.toMutableSet()

        if(!current.add(product)) {
            current.remove(product)
        } else {
            _selectedProducts.value = current
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val servicesState: StateFlow<FeatureState<List<ServiceWithEmployees>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getServicesByUserIdUseCase(userId)
                }

                emit(
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("Services").e("ERROR: on Fetching Services in MyProducts $e")
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