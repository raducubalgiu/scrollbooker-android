package com.example.scrollbooker.feature.appointments.presentation.tabs.business

import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.feature.appointments.domain.model.Product
import com.example.scrollbooker.feature.appointments.domain.model.UserAppointment

val dummyBusinessAppointments = listOf(
    Appointment(
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "finished",
        product = Product(
            id = 1,
            name = "ITP",
            price = 150,
            priceWithDiscount = 150,
            discount = 0,
            currency = "RON"
        ),
        user = UserAppointment(
            id = 1,
            avatar = "",
            fullName = "Radu Balgiu",
            username = "radu_balgiu",
            ratingsAverage = 4.5,
            profession = "Creator"
        ),
        isCustomer = true
    ),
    Appointment(
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "in_progress",
        product = Product(
            id = 1,
            name = "Tuns Simplu",
            price = 100,
            priceWithDiscount = 100,
            discount = 0,
            currency = "RON"
        ),
        user = UserAppointment(
            id = 1,
            avatar = "",
            fullName = "Cristiano Ronaldo",
            username = "cristiano",
            ratingsAverage = 4.5,
            profession = "Creator"
        ),
        isCustomer = true
    ),
    Appointment(
        startDate = "2025-05-22T09:00:00",
        endDate = "2025-05-22T10:00:00",
        channel = "own_client",
        status = "canceled",
        product = Product(
            id = 1,
            name = "Consultatie Stomatologie",
            price = 200,
            priceWithDiscount = 200,
            discount = 0,
            currency = "RON"
        ),
        user = UserAppointment(
            id = 1,
            avatar = "",
            fullName = "Lionel Messi",
            username = "leo_messi",
            ratingsAverage = 4.2,
            profession = "Creator"
        ),
        isCustomer = true
    ),
)