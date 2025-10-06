package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentOwnClientCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate

fun AppointmentOwnClientCreate.toDto(): AppointmentOwnClientCreateDto {
    return AppointmentOwnClientCreateDto(
        startDate = startDate,
        endDate = endDate,
        serviceId = serviceId,
        productId = productId,
        currencyId = currencyId,
        customerFullname = customerFullname,
        serviceName = serviceName,
        productName = productName,
        productPrice = productPrice,
        productPriceWithDiscount = productPriceWithDiscount,
        productDiscount = productDiscount,
        productDuration = productDuration,
    )
}