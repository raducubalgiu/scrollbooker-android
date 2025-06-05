package com.example.scrollbooker.feature.appointments.presentation.tabs.client

import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.feature.appointments.domain.model.AppointmentProduct
import com.example.scrollbooker.feature.appointments.domain.model.AppointmentUser

val dummyClientAppointments = listOf(
    Appointment(
        id = 1,
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "canceled",
        product = AppointmentProduct(
            id = 1,
            name = "ITP",
            price = 150,
            priceWithDiscount = 150,
            discount = 0,
            currency = "RON"
        ),
        user = AppointmentUser(
            id = 1,
            avatar = "",
            fullName = "ITP Dristor",
            username = "",
            profession = "Service Auto"
        ),
        isCustomer = false
    ),
    Appointment(
        id = 2,
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "in_progress",
        product = AppointmentProduct(
            id = 1,
            name = "Tuns Simplu",
            price = 100,
            priceWithDiscount = 100,
            discount = 0,
            currency = "RON"
        ),
        user = AppointmentUser(
            id = 1,
            avatar = "",
            fullName = "House Of Barbers",
            username = "",
            profession = "Frizerie"
        ),
        isCustomer = false
    ),
    Appointment(
        id = 3,
        startDate = "2025-05-22T09:00:00",
        endDate = "2025-05-22T10:00:00",
        channel = "own_client",
        status = "in_progress",
        product = AppointmentProduct(
            id = 1,
            name = "Consultatie Stomatologie",
            price = 200,
            priceWithDiscount = 200,
            discount = 0,
            currency = "RON"
        ),
        user = AppointmentUser(
            id = 1,
            avatar = "",
            fullName = "Delta Clinic Dent",
            username = "",
            profession = "Cabinet stomatologic"
        ),
        isCustomer = false
    ),
    Appointment(
        id = 4,
        startDate = "2025-05-21T13:00:00",
        endDate = "2025-05-21T14:00:00",
        channel = "scroll_booker",
        status = "finished",
        product = AppointmentProduct(
            id = 1,
            name = "Pensat",
            price = 100,
            priceWithDiscount = 50,
            discount = 50,
            currency = "RON"
        ),
        user = AppointmentUser(
            id = 1,
            avatar = "",
            fullName = "Salon Michelle",
            username = "",
            profession = "Salon de infrumusetare"
        ),
        isCustomer = false
    ),
)