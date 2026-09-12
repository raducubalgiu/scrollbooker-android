package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentOwnClientCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate

fun AppointmentOwnClientCreate.toDto(): AppointmentOwnClientCreateDto {
    return AppointmentOwnClientCreateDto(
        startDate = startDate,
        endDate = endDate,
        userId = userId,
        businessClientId = businessClientId,
        paymentCurrencyId = paymentCurrencyId,
        productVariants = productVariants,
    )
}
