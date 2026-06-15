package com.example.scrollbooker.ui.booking

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductOfferingCreateDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductVariantCreateDto
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering

data class SelectedBookingItem(
    val productId: Int,
    val variantId: Int,
    val variantDuration: Int,
    val offerings: List<ProductOffering>,
    val productName: String,
    val variantName: String
)

fun List<SelectedBookingItem>.toProductVariantsDto(): List<AppointmentProductVariantCreateDto> {
    return this.map { item ->
        val firstOffering = item.offerings.firstOrNull()

        AppointmentProductVariantCreateDto(
            id = item.variantId,
            offering = AppointmentProductOfferingCreateDto(
                userId = firstOffering?.user?.id
                    ?: throw IllegalArgumentException("No offering available for variant ${item.variantId}")
            )
        )
    }
}
