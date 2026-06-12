package com.example.scrollbooker.ui.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import javax.inject.Inject

data class SelectedBookingItem(
    val productId: Int,
    val variantId: Int,
    val variantDuration: Int,
    val offerings: List<ProductOffering>,
    val productName: String,
    val variantName: String
)

data class BookingTotals(
    val totalPrice: BigDecimal,
    val totalDuration: Int
)

@HiltViewModel
class BookingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase
): ViewModel() {
    val businessId: Int = checkNotNull(savedStateHandle["businessId"]) {
        "businessId mandatory parameter is missing in Booking flow"
    }
    val businessOwnerId: Int = checkNotNull(savedStateHandle["businessOwnerId"]) {
        "businessOwnerId mandatory parameter is missing in Booking flow"
    }
    val userId: Int = checkNotNull(savedStateHandle["userId"]) {
        "userId mandatory parameter is missing in Booking flow"
    }
    val source: String = checkNotNull(savedStateHandle["source"]) {
        "source mandatory parameter is missing in Booking flow"
    }

    val initialSelectedProductId: Int = savedStateHandle["selectedProductId"] ?: -1

    private val _isInitialProductProcessed = MutableStateFlow(false)
    val isInitialProductProcessed = _isInitialProductProcessed.asStateFlow()

    fun markInitialProductAsProcessed() {
        _isInitialProductProcessed.value = true
    }

    private val _selectedBookingItems = MutableStateFlow<List<SelectedBookingItem>>(emptyList())
    val selectedBookingItems: StateFlow<List<SelectedBookingItem>> =
        _selectedBookingItems.asStateFlow()

    val bookingTotals: StateFlow<BookingTotals> = selectedBookingItems
        .map { items ->
            val sumPrice = items.sumOf { item ->
                item.offerings.sumOf { offering -> offering.priceWithDiscount }
            }

            val sumDuration = items.sumOf { item -> item.variantDuration }

            BookingTotals(
                totalPrice = sumPrice,
                totalDuration = sumDuration
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookingTotals(BigDecimal.ZERO, 0)
        )

    val productsState: StateFlow<FeatureState<UserProducts>> = flow {
        emit(FeatureState.Loading)

        val result = withVisibleLoading(minLoadingMs = 400L) {
            getProductsByBusinessIdAndEmployeeIdUseCase(
                businessId = businessId,
                employeeId = if(businessOwnerId != userId) userId else null,
                onlyServicesWithProducts = true
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

    fun selectBookingItem(item: SelectedBookingItem) {
        val currentItems = _selectedBookingItems.value.toMutableList()
        val existingItemWithSameProduct = currentItems.find { it.productId == item.productId }

        if (existingItemWithSameProduct != null) {
            if (existingItemWithSameProduct.variantId == item.variantId) {
                currentItems.remove(existingItemWithSameProduct)
            } else {
                val index = currentItems.indexOf(existingItemWithSameProduct)
                if (index != -1) {
                    currentItems[index] = item
                }
            }
        } else {
            currentItems.add(item)
        }

        _selectedBookingItems.value = currentItems
    }
}