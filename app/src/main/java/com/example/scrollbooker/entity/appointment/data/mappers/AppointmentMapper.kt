package com.example.scrollbooker.entity.appointment.data.mappers

import com.example.scrollbooker.entity.appointment.data.remote.AppointmentDto
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentProductDto
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentUserDto
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.appointment.domain.model.AppointmentProduct
import com.example.scrollbooker.entity.appointment.domain.model.AppointmentUser

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id,
        startDate = startDate,
        endDate = endDate,
        channel = channel,
        status = status,
        product = product.toDomain(),
        user = user.toDomain(),
        isCustomer = isCustomer,
    )
}

fun AppointmentProductDto.toDomain(): AppointmentProduct {
    return AppointmentProduct(
        id = id,
        name = name,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        currency = currency,
    )
}

fun AppointmentUserDto.toDomain(): AppointmentUser {
    return AppointmentUser(
        id = id,
        avatar = avatar,
        fullName = fullName,
        username = username,
        profession = profession,
    )
}