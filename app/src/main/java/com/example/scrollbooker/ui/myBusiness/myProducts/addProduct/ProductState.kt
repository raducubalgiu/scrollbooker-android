package com.example.scrollbooker.ui.myBusiness.myProducts.addProduct

import android.content.Context
import androidx.compose.runtime.Immutable
import com.example.scrollbooker.core.extensions.toBigDecimalOrNull
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.checkMinMax
import com.example.scrollbooker.core.util.checkRequired
import java.math.BigDecimal

@Immutable
data class ProductOfferingValidation(
    val isValid: Boolean = true,
    val priceError: String? = null,
    val discountError: String? = null,
)

@Immutable
data class ProductOfferingState(
    val userId: String = "",
    val price: String = "",
    val discount: String = "",
    val priceWithDiscount: String = "",
    val isSelected: Boolean
) {
    fun validate(context: Context): ProductOfferingValidation {
        val priceError = checkRequired(context, price)
        val discountError = checkMinMax(context, discount, 0, 100)

        val allFields = listOf(priceError, discountError)

        return ProductOfferingValidation(
            isValid = allFields.all { it == null },
            priceError = priceError,
            discountError = discountError
        )
    }
}

@Immutable
data class ProductVariantValidation(
    val isValid: Boolean = true,
    val nameError: String? = null,
    val durationError: String? = null,
    val offeringsError: String? = null,
    val offeringValidations: List<ProductOfferingValidation> = emptyList()
)

@Immutable
data class ProductVariantState(
    val name: String = "",
    val duration: String = "",
    val offerings: List<ProductOfferingState> = emptyList()
) {
    val selectedOfferings: List<ProductOfferingState>
        get() = offerings.filter { it.isSelected }

    val hasDifferentOfferings: Boolean
        get() = selectedOfferings
            .mapNotNull { it.priceWithDiscount.toBigDecimalOrNull() }
            .distinct()
            .size > 1

    val cheapestOffering: ProductOfferingState?
        get() = selectedOfferings.minByOrNull {
            it.priceWithDiscount.toBigDecimalOrNull() ?: BigDecimal.ZERO
        }

    fun validate(context: Context): ProductVariantValidation {
        val nameError = checkLength(context, name, minLength = 2, maxLength = 50)
        val durationError = checkRequired(context, duration)

        val offeringValidations = offerings.map { it.validate(context) }
        val offeringsError = when {
            offerings.isEmpty() -> "Este necesar să adaugi cel puțin un preț."
            else -> null
        }

        val allFields = listOf(nameError, durationError, offeringsError)
        val offeringsValid = offeringValidations.all { it.isValid }

        return ProductVariantValidation(
            isValid = allFields.all { it == null } && offeringsValid,
            nameError = nameError,
            durationError = durationError,
            offeringsError = offeringsError,
            offeringValidations = offeringValidations
        )
    }
}

@Immutable
data class AddProductValidation(
    val isValid: Boolean = true,
    val nameError: String? = null,
    val serviceDomainIdError: String? = null,
    val serviceIdError: String? = null,
    val variantsError: String? = null,
    val variantValidations: List<ProductVariantValidation> = emptyList()
)

@Immutable
data class ProductState(
    val name: String = "",
    val description: String = "",
    val serviceDomainId: String = "",
    val serviceId: String = "",
    val currencyId: String = "1",
    val variants: List<ProductVariantState> = emptyList()
) {
    fun validate(context: Context): AddProductValidation {
        val nameError = checkLength(context, name, minLength = 3, maxLength = 100)
        val serviceDomainIdError = checkRequired(context, serviceDomainId)
        val serviceIdError = checkRequired(context, serviceId)

        val variantValidations = variants.map { it.validate(context) }
        val variantsError = when {
            variants.isEmpty() -> "Este necesar sa adaugi cel puțin un variant."
            else -> null
        }

        val allFields = listOf(
            nameError,
            serviceDomainIdError,
            serviceIdError,
            variantsError
        )
        val variantsValid = variantValidations.all { it.isValid }

        return AddProductValidation(
            isValid = allFields.all { it == null } && variantsValid,
            nameError = nameError,
            serviceDomainIdError = serviceDomainIdError,
            serviceIdError = serviceIdError,
            variantsError = variantsError,
            variantValidations = variantValidations
        )
    }
}