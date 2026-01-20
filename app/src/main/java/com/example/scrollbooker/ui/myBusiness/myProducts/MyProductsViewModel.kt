package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase

import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getServicesByBusinessIdUseCase: GetServicesByBusinessIdUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
): ViewModel() {
    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct

    private val _productsReloadTrigger = mutableIntStateOf(0)
    val productsReloadTrigger: State<Int> = _productsReloadTrigger

    init {
        loadServices()
    }

    private val userIdFlow = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    fun refreshProducts(serviceId: Int?) {
        productsFlowCache.remove(serviceId)
        _productsReloadTrigger.intValue++
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadProducts(serviceId: Int, employeeId: Int?): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            userIdFlow.flatMapLatest { userId ->
                getProductsByUserIdAndServiceIdUseCase(userId, serviceId, employeeId)
                    .cachedIn(viewModelScope)
            }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }

    fun deleteProduct(product: Product, serviceId: Int) {
        viewModelScope.launch {
            _isSaving.value = true
            _selectedProduct.value = product

            if(_selectedProduct.value == null) {
                throw IllegalStateException("Product is not devined")
            }

            val result = withVisibleLoading {
                deleteProductUseCase(_selectedProduct.value!!.id)
            }

            result
                .onSuccess {
                    refreshProducts(serviceId)

                    _isSaving.value = false
                    _selectedProduct.value = null
                }
                .onFailure { e ->
                    _isSaving.value = false
                    _selectedProduct.value = null
                    Timber.tag("Products").e("ERROR: on Deleting Product in MyProducts $e")
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
}