package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentCreate

fun AppointmentCreate.toDto(): AppointmentCreateDto {
    return AppointmentCreateDto(
        startDate = startDate,
        endDate = endDate,
        channel = channel,
        userId = userId,
        businessId = businessId,
        customerId = customerId,
        currencyId = currencyId,
        serviceId = serviceId,
        productId = productId,
        fullname = fullname,
        serviceName = serviceName,
        productName = productName,
        productPrice = productPrice,
        productPriceWithDiscount = productPriceWithDiscount,
        productDiscount = productDiscount,
    )
}