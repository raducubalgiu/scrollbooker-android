package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentScrollBookerCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate

fun AppointmentScrollBookerCreate.toDto(): AppointmentScrollBookerCreateDto {
    return AppointmentScrollBookerCreateDto(
        startDate = startDate,
        endDate = endDate,
        userId = userId,
        currencyId = currencyId,
        serviceId = serviceId,
        productId = productId,
    )
}