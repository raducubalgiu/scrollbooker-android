package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentCustomProductDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentOwnClientCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate

fun AppointmentOwnClientCreate.toDto(): AppointmentOwnClientCreateDto {
    return AppointmentOwnClientCreateDto(
        startDate = startDate,
        endDate = endDate,
        userId = userId,
        customerFullname = customerFullname,
        paymentCurrencyId = paymentCurrencyId,
        customProduct = AppointmentCustomProductDto(
            productName = customProduct.productName,
            price = customProduct.price,
            priceWithDiscount = customProduct.priceWithDiscount,
            discount = customProduct.discount,
            duration = customProduct.duration,
        ),
        productVariants = null,
    )
}