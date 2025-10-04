package com.example.scrollbooker.entity.booking.appointment.data.mappers
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBusinessDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentUserDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.BusinessCoordinatesDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentBusiness
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentProduct
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import org.threeten.bp.ZonedDateTime

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id,
        startDate = ZonedDateTime.parse(startDate),
        endDate = ZonedDateTime.parse(endDate),
        channel = channel,
        status = status,
        message = message,
        product = product.toDomain(),
        user = user.toDomain(),
        isCustomer = isCustomer,
        business = business.toDomain()
    )
}

fun AppointmentProductDto.toDomain(): AppointmentProduct {
    return AppointmentProduct(
        id = id,
        name = name,
        price = price,
        priceWithDiscount = priceWithDiscount,
        duration = duration,
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

fun BusinessCoordinatesDto.toDomain(): BusinessCoordinates {
    return BusinessCoordinates(
        lat = lat,
        lng = lng
    )
}

fun AppointmentBusinessDto.toDomain(): AppointmentBusiness {
    return AppointmentBusiness(
        address = address,
        coordinates = coordinates.toDomain()
    )
}