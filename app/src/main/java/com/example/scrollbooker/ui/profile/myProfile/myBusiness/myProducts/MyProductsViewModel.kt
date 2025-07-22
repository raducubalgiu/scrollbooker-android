package com.example.scrollbooker.ui.profile.myProfile.myBusiness.myProducts
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByBusinessTypeUseCase
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
    private val getProductUseCase: GetProductUseCase,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase,
    private val getFiltersByBusinessTypeUseCase: GetFiltersByBusinessTypeUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
): ViewModel() {
    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val _currenciesState = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val currenciesState: StateFlow<FeatureState<List<Currency>>> = _currenciesState

    private val _filtersState = MutableStateFlow<FeatureState<List<Filter>>>(FeatureState.Loading)
    val filtersState: StateFlow<FeatureState<List<Filter>>> = _filtersState

    private val _selectedFilterOptions = mutableStateOf<Map<String, String>>(emptyMap())
    val selectedFilterOptions: State<Map<String, String>> = _selectedFilterOptions

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _product = MutableStateFlow<FeatureState<Product>>(FeatureState.Loading)
    val product: StateFlow<FeatureState<Product>> = _product

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
    fun loadProducts(serviceId: Int): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            userIdFlow.flatMapLatest { userId ->
                getProductsByUserIdAndServiceIdUseCase(userId, serviceId)
                    .cachedIn(viewModelScope)
            }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }

    fun setProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun updateSelectedFilter(key: String, value: String) {
        _selectedFilterOptions.value = _selectedFilterOptions.value.toMutableMap().apply {
            this[key] = value
        }
    }

    fun removeSelectedFilters() {
        _selectedFilterOptions.value = emptyMap()
    }

    fun createProduct(
        name: String,
        description: String,
        price: String,
        priceWithDiscount: String,
        discount: String,
        duration: String,
        serviceId: String,
        currencyId: String,
    ) {
        viewModelScope.launch {
            _isSaving.value = true

            val subFilters = selectedFilterOptions.value.values.mapNotNull { it.toIntOrNull() }
            val businessId = authDataStore.getBusinessId().firstOrNull()

            if(businessId == null) {
                throw kotlin.IllegalStateException("Business Id not found in Auth Data Store")
            }

            val response = withVisibleLoading {
                createProductUseCase(
                    productCreate = ProductCreate(
                        name = name,
                        description = description,
                        price = price.toBigDecimal(),
                        priceWithDiscount = priceWithDiscount.toBigDecimal(),
                        discount = discount.toBigDecimal(),
                        duration = duration.toInt(),
                        serviceId = serviceId.toInt(),
                        businessId = businessId,
                        currencyId = currencyId.toInt()
                    ),
                    subFilters = subFilters
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
                    Timber.tag("Products").e("SHOULD RUN!!!")
                }
                .onFailure { e ->
                    _isSaving.value = false
                    _selectedProduct.value = null
                    Timber.tag("Products").e("ERROR: on Deleting Product in MyProducts $e")
                }
        }
    }

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _product.value = FeatureState.Loading

            val result = withVisibleLoading { getProductUseCase(productId) }

            result
                .onSuccess { response ->
                    _product.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Products").e("ERROR: on Fetching Product in EditProductScreen $e")
                    _product.value = FeatureState.Error()
                }
        }
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

    fun loadFilters() {
        viewModelScope.launch {
            _filtersState.value = FeatureState.Loading
            _filtersState.value = withVisibleLoading { getFiltersByBusinessTypeUseCase() }
        }
    }
}