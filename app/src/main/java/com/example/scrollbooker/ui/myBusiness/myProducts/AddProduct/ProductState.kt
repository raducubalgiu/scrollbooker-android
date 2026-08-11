package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct

import android.content.Context
import androidx.compose.runtime.Immutable
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.checkRequired

@Immutable
data class AddProductValidation(
    val isValid: Boolean = true,
    val nameError: String? = null,
    val serviceDomainIdError: String? = null,
    val serviceIdError: String? = null,
)

@Immutable
data class ProductState(
    val name: String = "",
    val description: String = "",
    val serviceDomainId: String = "",
    val serviceId: String = "",
    val currencyId: String = ""
) {
    fun validate(context: Context): AddProductValidation {
        val nameError = checkLength(context, name, minLength = 3, maxLength = 100)
        val serviceDomainIdError = checkRequired(context, serviceDomainId)
        val serviceIdError = checkRequired(context, serviceId)

        val allFields = listOf(
            nameError,
            serviceDomainIdError,
            serviceIdError
        )

        return AddProductValidation(
            isValid = allFields.all { it == null },
            nameError = nameError,
            serviceDomainIdError = serviceDomainIdError,
            serviceIdError = serviceIdError
        )
    }
}