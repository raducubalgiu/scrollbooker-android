package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase

import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    authDataStore: AuthDataStore,
): ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Long>(replay = 1).apply {
        tryEmit(System.currentTimeMillis())
    }

    private val authConfigFlow = combine(
        authDataStore.getUserId(),
        authDataStore.getBusinessId(),
        authDataStore.getBusinessOwnerId()
    ) { userId, businessId, businessOwnerId ->
        val resolvedEmployeeId = if (businessOwnerId == userId) null else userId

        AuthConfig(businessId = businessId, employeeId = resolvedEmployeeId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productsState: StateFlow<FeatureState<UserProducts>> = combine(
        authConfigFlow,
        refreshTrigger
    ) { config, _ -> config }
        .flatMapLatest { config ->
            flow {
                emit(FeatureState.Loading)

                if (config.businessId != null) {
                    val result = withVisibleLoading {
                        getProductsByBusinessIdAndEmployeeIdUseCase(
                            businessId = config.businessId,
                            employeeId = config.employeeId,
                            onlyServicesWithProducts = false
                        )
                    }
                    emit(result)
                } else {
                    emit(FeatureState.Error(IllegalStateException("Missing Business ID")))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    fun refreshProducts() {
        viewModelScope.launch {
            refreshTrigger.emit(System.currentTimeMillis())
        }
    }

    private data class AuthConfig(
        val businessId: Int?,
        val employeeId: Int?
    )
}