package com.example.scrollbooker.entity.booking.booking.data.mappers

import com.example.scrollbooker.entity.booking.booking.data.remote.BookingFlowBusinessDto
import com.example.scrollbooker.entity.booking.booking.data.remote.BookingFlowDto
import com.example.scrollbooker.entity.booking.booking.data.remote.BookingFlowUserDto
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowBusiness
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain

fun BookingFlowDto.toDomain(): BookingFlow {
    return BookingFlow(
        business = business.toDomain(),
        products = products.toDomain(),
        employees = employees.map { it.toDomain() }
    )
}

fun BookingFlowBusinessDto.toDomain(): BookingFlowBusiness {
    return BookingFlowBusiness(
        owner = owner.toDomain(),
        hasEmployees = hasEmployees,
        formattedAddress = formattedAddress
    )
}

fun BookingFlowUserDto.toDomain(): BookingFlowUser {
    return BookingFlowUser(
        id = id,
        username = username,
        fullName = fullName,
        profession = profession,
        avatar = avatar
    )
}