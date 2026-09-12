package com.example.scrollbooker.entity.booking.appointment.domain.model

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductVariantCreateDto

data class AppointmentOwnClientCreate(
    val startDate: String,
    val endDate: String,
    val userId: Int,
    val businessClientId: Int,
    val paymentCurrencyId: Int = 1,
    val productVariants: List<AppointmentProductVariantCreateDto>,
)
