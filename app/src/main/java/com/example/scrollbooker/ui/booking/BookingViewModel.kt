package com.example.scrollbooker.ui.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase
): ViewModel() {
    val businessId: Int = checkNotNull(savedStateHandle["businessId"]) {
        "businessId mandatory parameter is missing in Booking flow"
    }

    val employeeId: Int? = savedStateHandle.get<Int>("employeeId")?.takeIf { it != -1 }

    val productsState: StateFlow<FeatureState<List<BusinessServicesWithProducts>>> = flow {
        emit(FeatureState.Loading)

        val result = withVisibleLoading {
            getProductsByBusinessIdAndEmployeeIdUseCase(
                businessId = businessId,
                employeeId = employeeId
            )
        }
        emit(result)
    }.catch { e ->
        emit(FeatureState.Error(e as? Exception ?: Exception(e)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeatureState.Loading
    )
}