package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Immutable
data class OwnClientFormStateState(
    val customerName: String = "",
    val serviceName: String = "",
    val productName: String = "",
    val price: String = "",
    val priceWithDiscount: String = "",
    val discount: String = "0",
    val selectedServiceId: String? = null,
    val selectedProductId: String? = null,
    val selectedCurrencyId: String? = null
) {
    companion object {
        val Saver = listSaver<OwnClientFormStateState, String?>(
            save = { s ->
                listOf(
                    s.customerName,
                    s.serviceName,
                    s.productName,
                    s.price,
                    s.priceWithDiscount,
                    s.discount,
                    s.selectedServiceId,
                    s.selectedProductId,
                    s.selectedCurrencyId
                )
            },
            restore = { list ->
                OwnClientFormStateState(
                    customerName = list.getOrNull(0) ?: "",
                    serviceName = list.getOrNull(1) ?: "",
                    productName = list.getOrNull(2) ?: "",
                    price = list.getOrNull(3) ?: "",
                    priceWithDiscount = list.getOrNull(4) ?: "",
                    discount = list.getOrNull(5) ?: "0",
                    selectedServiceId = list.getOrNull(6) ?: "",
                    selectedProductId = list.getOrNull(7) ?: "",
                    selectedCurrencyId = list.getOrNull(8) ?: ""
                )
            }
        )
    }
}

sealed interface OwnClientFormEvent {
    data class CustomerNameChanged(val v: String): OwnClientFormEvent
    data class ServiceNameChanged(val v: String): OwnClientFormEvent
    data class ProductNameChanged(val v: String): OwnClientFormEvent
    data class PriceChanged(val v: String): OwnClientFormEvent
    data class PriceWithDiscountChanged(val v: String): OwnClientFormEvent
    data class DiscountChanged(val v: String): OwnClientFormEvent
    data class ServiceSelected(val id: String?): OwnClientFormEvent
    data class ProductSelected(val id: String?): OwnClientFormEvent
    data class CurrencySelected(val id: String?): OwnClientFormEvent
}

@Composable
fun rememberOwnClientFormState(): Pair<OwnClientFormStateState,(OwnClientFormEvent) -> Unit> {
    var state by rememberSaveable(stateSaver = OwnClientFormStateState.Saver) {
        mutableStateOf(OwnClientFormStateState())
    }

    val dispatch: (OwnClientFormEvent) -> Unit = remember {
        { e ->
            state = when(e) {
                is OwnClientFormEvent.CustomerNameChanged -> state.copy(customerName = e.v)
                is OwnClientFormEvent.ServiceNameChanged -> state.copy(serviceName = e.v)
                is OwnClientFormEvent.ProductNameChanged -> state.copy(productName = e.v)
                is OwnClientFormEvent.PriceChanged -> state.copy(price = e.v)
                is OwnClientFormEvent.PriceWithDiscountChanged -> state.copy(priceWithDiscount = e.v)
                is OwnClientFormEvent.DiscountChanged -> state.copy(discount = e.v)
                is OwnClientFormEvent.ServiceSelected -> state.copy(selectedServiceId = e.id)
                is OwnClientFormEvent.ProductSelected -> state.copy(selectedProductId = e.id)
                is OwnClientFormEvent.CurrencySelected -> state.copy(selectedCurrencyId = e.id)
            }
        }
    }
    return state to dispatch
}