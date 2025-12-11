package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.checkMinMax
import com.example.scrollbooker.core.util.checkRequired

@Immutable
data class OwnClientFormStateState(
    val customerName: String = "",
    val productName: String = "",
    val price: String = "0",
    val discount: String = "0"
) {
    val customerNameMaxLength = 50
    val productNameMaxLength = 100

    fun validate(context: Context): OwnClientValidationResult {
        val customerNameError = checkLength(context, customerName, minLength = 3, maxLength = customerNameMaxLength)
        val productNameError = checkLength(context, productName, minLength = 3, maxLength = productNameMaxLength)
        val priceError = checkRequired(context, price)
        val discountError = checkMinMax(context, discount, min=0, max=100)

        return OwnClientValidationResult(
            isValid = listOf(
                customerNameError,
                productNameError,
                priceError,
                discountError,
            ).all { it == null },
            customerNameMaxLength = customerNameMaxLength,
            customerNameError = customerNameError,
            productNameMaxLength = productNameMaxLength,
            productNameError = productNameError,
            priceError = priceError,
            discountError = discountError
        )
    }

    companion object {
        val Saver = listSaver<OwnClientFormStateState, String?>(
            save = { s ->
                listOf(
                    s.customerName,
                    s.productName,
                    s.price,
                    s.discount,
                )
            },
            restore = { list ->
                OwnClientFormStateState(
                    customerName = list.getOrNull(0) ?: "",
                    productName = list.getOrNull(1) ?: "",
                    price = list.getOrNull(2) ?: "",
                    discount = list.getOrNull(3) ?: "0",
                )
            }
        )
    }
}

sealed interface OwnClientFormEvent {
    data class CustomerNameChanged(val v: String): OwnClientFormEvent
    data class ProductNameChanged(val v: String): OwnClientFormEvent
    data class PriceChanged(val v: String): OwnClientFormEvent
    data class DiscountChanged(val v: String): OwnClientFormEvent
}

data class OwnClientValidationResult(
    val isValid: Boolean,
    val customerNameMaxLength: Int? = null,
    val customerNameError: String? = null,
    val productNameMaxLength: Int? = null,
    val productNameError: String? = null,
    val priceError: String? = null,
    val discountError: String? = null,
)

@Composable
fun rememberOwnClientFormState(): Pair<OwnClientFormStateState,(OwnClientFormEvent) -> Unit> {
    var state by rememberSaveable(stateSaver = OwnClientFormStateState.Saver) {
        mutableStateOf(OwnClientFormStateState())
    }

    val dispatch: (OwnClientFormEvent) -> Unit = remember {
        { e ->
            state = when(e) {
                is OwnClientFormEvent.CustomerNameChanged -> state.copy(customerName = e.v)
                is OwnClientFormEvent.ProductNameChanged -> state.copy(productName = e.v)
                is OwnClientFormEvent.PriceChanged -> state.copy(price = e.v)
                is OwnClientFormEvent.DiscountChanged -> state.copy(discount = e.v)
            }
        }
    }
    return state to dispatch
}