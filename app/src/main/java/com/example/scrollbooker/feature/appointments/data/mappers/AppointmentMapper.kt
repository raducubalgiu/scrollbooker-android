package com.example.scrollbooker.feature.appointments.data.mappers

import com.example.scrollbooker.feature.appointments.data.remote.AppointmentDto
import com.example.scrollbooker.feature.appointments.data.remote.ProductDto
import com.example.scrollbooker.feature.appointments.data.remote.UserAppointmentDto
import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.feature.appointments.domain.model.Product
import com.example.scrollbooker.feature.appointments.domain.model.UserAppointment

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        startDate = startDate,
        endDate = endDate,
        channel = channel,
        status = status,
        product = product.toDomain(),
        user = user.toDomain(),
        isCustomer = isCustomer,
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        currency = currency,
    )
}

fun UserAppointmentDto.toDomain(): UserAppointment {
    return UserAppointment(
        id = id,
        avatar = avatar,
        fullName = fullName,
        username = username,
        ratingsAverage = ratingsAverage,
        profession = profession,
    )
}