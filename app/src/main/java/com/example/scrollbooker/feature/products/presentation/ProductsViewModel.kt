package com.example.scrollbooker.feature.products.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.scrollbooker.feature.products.domain.useCase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    //private val getProductUseCase: GetProductUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    //private val productId: Int = savedStateHandle["productId"] ?: error("Missing productId")

    private val _userId = MutableStateFlow<Int?>(null)

    //private val _productState = MutableStateFlow<FeatureState<Product>>(FeatureState.Loading)
    //val productState: StateFlow<FeatureState<Product>> = _productState

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = _userId
        .filterNotNull()
        .flatMapLatest { userId -> getProductsUseCase(userId) }
        .cachedIn(viewModelScope)

    fun loadProducts(userId: Int) {
        _userId.value = userId
    }

//    fun getProduct() {
//        viewModelScope.launch {
//            _productState.value = FeatureState.Loading
//
//            getProductUseCase(productId)
//                .onSuccess { product ->
//                    _productState.value = FeatureState.Success(product)
//                }
//                .onFailure { error ->
//                    Timber.tag("Get Product").e("ERROR: on Loading ProductId: $productId, $error")
//                    _productState.value = FeatureState.Error(error)
//                }
//            }
//        }
//    }
}