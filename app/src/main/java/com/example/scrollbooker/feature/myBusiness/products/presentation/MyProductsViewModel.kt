package com.example.scrollbooker.feature.myBusiness.products.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.feature.myBusiness.products.domain.model.Product
import com.example.scrollbooker.feature.myBusiness.products.domain.useCase.GetProductsByUserIdAndServiceIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getProductsByUserIdAndServiceIdUseCase: GetProductsByUserIdAndServiceIdUseCase
): ViewModel() {

    private val userIdFlow = authDataStore.getUserId()
        .filterNotNull()
        .distinctUntilChanged()
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    private val productsFlowCache = mutableMapOf<Int, Flow<PagingData<Product>>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProductsFlow(serviceId: Int): Flow<PagingData<Product>> {
        return productsFlowCache.getOrPut(serviceId) {
            userIdFlow.flatMapLatest { userId ->
                getProductsByUserIdAndServiceIdUseCase(userId, serviceId)
                    .cachedIn(viewModelScope)
            }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)
        }
    }
}