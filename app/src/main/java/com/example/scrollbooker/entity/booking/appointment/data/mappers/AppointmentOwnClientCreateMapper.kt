package com.example.scrollbooker.entity.booking.appointment.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentOwnClientCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate

fun AppointmentOwnClientCreate.toDto(): AppointmentOwnClientCreateDto {
    return AppointmentOwnClientCreateDto(
        startDate = startDate,
        endDate = endDate,
        customerFullname = customerFullname,
        productName = productName,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        duration = duration,
    )
}