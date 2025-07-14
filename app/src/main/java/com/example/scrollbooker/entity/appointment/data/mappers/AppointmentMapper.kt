package com.example.scrollbooker.entity.appointment.data.mappers

import com.example.scrollbooker.entity.appointment.data.remote.AppointmentDto
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentProductDto
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentUserDto
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.appointment.domain.model.AppointmentProduct
import com.example.scrollbooker.entity.appointment.domain.model.AppointmentUser
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id,
        startDate = ZonedDateTime.parse(startDate),
        endDate = ZonedDateTime.parse(endDate),
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
        exchangeRate = exchangeRate
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