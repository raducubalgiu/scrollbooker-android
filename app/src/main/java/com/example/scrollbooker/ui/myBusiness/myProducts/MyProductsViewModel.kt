package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.DeleteProductUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase

import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProductsViewModel @Inject constructor(
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
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

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedServices = authDataStore.getBusinessId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { businessId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getSelectedServiceDomainsWithServicesByBusinessIdUseCase(businessId)
                }
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

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

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            _isSaving.value = true

            val result = withVisibleLoading {
                deleteProductUseCase(productId)
            }

            result
                .onSuccess {
                    refreshProducts()
                    _events.tryEmit(
                        SnackBarUiEvent.Show(
                            message = UiText.Resource(R.string.productDeleteSuccess)
                        )
                    )
                    _isSaving.value = false
                }
                .onFailure { e ->
                    Timber.tag("Delete Product").e("ERROR: on Deleting Product $productId - ${e.message}")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    _isSaving.value = false
                }
        }
    }

    private data class AuthConfig(
        val businessId: Int?,
        val employeeId: Int?
    )
}