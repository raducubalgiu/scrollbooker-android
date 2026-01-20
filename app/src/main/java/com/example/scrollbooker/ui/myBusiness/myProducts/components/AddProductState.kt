package com.example.scrollbooker.ui.myBusiness.myProducts.components

import android.content.Context
import androidx.compose.runtime.Immutable
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.checkMinMax
import com.example.scrollbooker.core.util.checkRequired

@Immutable
data class AddProductValidation(
    val isValid: Boolean = true,
    val nameError: String? = null,
    val priceError: String? = null,
    val discountError: String? = null,
    val durationError: String? = null,
    val serviceIdError: String? = null,
    val currencyIdError: String? = null
)

@Immutable
data class AddProductState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val priceWithDiscount: String = "",
    val discount: String = "0",
    val duration: String = "",
    val serviceId: String = "",
    val currencyId: String = "",
    val canBeBooked: Boolean = true
) {
    fun validate(context: Context): AddProductValidation {
        val nameError = checkLength(context, name, minLength = 3, maxLength = 100)
        val priceError = checkMinMax(context, price, min=10)
        val discountError = checkMinMax(context, discount, min=0, max=100)
        val durationError = checkMinMax(context, duration, min=15)
        val serviceIdError = checkRequired(context, serviceId)
        val currencyIdError = checkRequired(context, currencyId)

        val allFields = listOf(nameError, priceError, discountError, durationError, serviceIdError, currencyIdError)

        return AddProductValidation(
            isValid = allFields.all { it == null },
            nameError = nameError,
            priceError = priceError,
            discountError = discountError,
            durationError = durationError,
            serviceIdError = serviceIdError,
            currencyIdError = currencyIdError
        )
    }
}