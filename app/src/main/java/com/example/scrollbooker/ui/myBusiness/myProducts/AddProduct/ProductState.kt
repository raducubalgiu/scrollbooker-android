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
    val currencyId: String = "",
    val variants: List<ProductVariantState> = emptyList()
) {
    fun validate(context: Context): AddProductValidation {
        val nameError = checkLength(context, name, minLength = 3, maxLength = 100)
        val serviceDomainIdError = checkRequired(context, serviceDomainId)
        val serviceIdError = checkRequired(context, serviceId)

        val validatedVariants = variants.map { it.validate(context) }

        val allFields = listOf(
            nameError,
            serviceDomainIdError,
            serviceIdError
        )

        val isEverythingValid = allFields.all { it == null } && validatedVariants.all { it.isValid }

        return AddProductValidation(
            isValid = isEverythingValid,
            nameError = nameError,
            serviceDomainIdError = serviceDomainIdError,
            serviceIdError = serviceIdError
        )
    }
}


@Immutable
data class AddProductVariantValidation(
    val isValid: Boolean = true,
    val nameError: String? = "",
    val durationError: String? = "",
    val offerings: List<AddProductOfferingValidation> = emptyList()
)

@Immutable
data class ProductVariantState(
    val name: String,
    val duration: String,
    val offerings: List<ProductOfferingState>
) {
    fun validate(context: Context): AddProductVariantValidation {
        val nameError = checkRequired(context, name)
        val durationError = checkRequired(context, duration)
        val validatedOfferings = offerings.map { it.validate(context) }

        val isEverythingValid = nameError == null &&
                durationError == null &&
                validatedOfferings.all { it.isValid }

        return AddProductVariantValidation(
            isValid = isEverythingValid,
            nameError = nameError,
            durationError = durationError,
            offerings = validatedOfferings
        )
    }
}


@Immutable
data class AddProductOfferingValidation(
    val isValid: Boolean = true,
    val priceError: String? = null,
    val discountError: String? = null,
    val priceWithDiscountError: String? = null
)

@Immutable
data class ProductOfferingState(
    val userId: Int,
    val fullName: String,
    val isSelected: Boolean = false,
    val price: String = "",
    val discount: String = "0",
    val priceWithDiscount: String = ""
) {
    fun validate(context: Context): AddProductOfferingValidation {
        if (!isSelected) return AddProductOfferingValidation(isValid = true)

        val priceError = checkRequired(context, price)
        val discountError = checkRequired(context, discount)
        val priceWithDiscountError = checkRequired(context, priceWithDiscount)

        val allFields = listOf(priceError, discountError, priceWithDiscountError)

        return AddProductOfferingValidation(
            isValid = allFields.all { it == null },
            priceError = priceError,
            discountError = discountError,
            priceWithDiscountError = priceWithDiscountError
        )
    }
}