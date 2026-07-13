package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct

import android.content.Context
import androidx.compose.runtime.Immutable
import com.example.scrollbooker.core.enums.ProductTypeEnum
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
    val serviceDomainIdError: String? = null,
    val serviceIdError: String? = null,
    val productTypeError: String? = null
)

@Immutable
data class ProductState(
    val name: String = "",
    val description: String = "",
    val duration: String = "",
    val serviceDomainId: String = "",
    val serviceId: String = "",
    val currencyId: String = "",
    val canBeBooked: Boolean = true,
    val type: ProductTypeEnum? = null,
    val sessionsCount: String = "",
    val validityDays: String = "",
) {
    fun validate(context: Context): AddProductValidation {
        val nameError = checkLength(context, name, minLength = 3, maxLength = 100)
        val durationError = checkMinMax(context, duration, min=15)
        val serviceDomainIdError = checkRequired(context,serviceDomainId)
        val serviceIdError = checkRequired(context, serviceId)
        val productTypeError = checkRequired(context, type?.key)

        val allFields = listOf(
            nameError,
            durationError,
            serviceDomainIdError,
            serviceIdError,
            productTypeError
        )

        return AddProductValidation(
            isValid = allFields.all { it == null },
            nameError = nameError,
            durationError = durationError,
            serviceDomainIdError = serviceDomainIdError,
            serviceIdError = serviceIdError,
            productTypeError = productTypeError
        )
    }
}