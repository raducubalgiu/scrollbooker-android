package com.example.scrollbooker.feature.appointments.presentation.tabs.client

import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.feature.appointments.domain.model.Product
import com.example.scrollbooker.feature.appointments.domain.model.User

val dummyClientAppointments = listOf(
    Appointment(
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "canceled",
        product = Product(
            id = 1,
            name = "ITP",
            price = 150,
            priceWithDiscount = 150,
            discount = 0,
            currency = "RON"
        ),
        user = User(
            id = 1,
            avatar = "",
            fullName = "ITP Dristor",
            username = "",
            ratingsAverage = 4.5,
            profession = "Service Auto"
        ),
        isCustomer = false
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
        user = User(
            id = 1,
            avatar = "",
            fullName = "House Of Barbers",
            username = "",
            ratingsAverage = 4.5,
            profession = "Frizerie"
        ),
        isCustomer = false
    ),
    Appointment(
        startDate = "2025-05-22T09:00:00",
        endDate = "2025-05-22T10:00:00",
        channel = "own_client",
        status = "in_progress",
        product = Product(
            id = 1,
            name = "Consultatie Stomatologie",
            price = 200,
            priceWithDiscount = 200,
            discount = 0,
            currency = "RON"
        ),
        user = User(
            id = 1,
            avatar = "",
            fullName = "Delta Clinic Dent",
            username = "",
            ratingsAverage = 4.2,
            profession = "Cabinet stomatologic"
        ),
        isCustomer = false
    ),
    Appointment(
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "finished",
        product = Product(
            id = 1,
            name = "Pensat",
            price = 100,
            priceWithDiscount = 50,
            discount = 50,
            currency = "RON"
        ),
        user = User(
            id = 1,
            avatar = "",
            fullName = "Salon Michelle",
            username = "",
            ratingsAverage = 4.2,
            profession = "Salon de infrumusetare"
        ),
        isCustomer = false
    ),
)