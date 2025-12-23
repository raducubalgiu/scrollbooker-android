package com.example.scrollbooker.ui.appointments

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentBusiness
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal

val fakeAppointmentsList = listOf(
    Appointment(
        id = 1,
        startDate = ZonedDateTime.now(),
        endDate = ZonedDateTime.now(),
        channel = AppointmentChannelEnum.SCROLL_BOOKER,
        status = AppointmentStatusEnum.FINISHED,
        isCustomer = false,
        products = emptyList(),
        user = AppointmentUser(
            id = 1,
            fullName = "Radu",
            username = "radu",
            avatar = null,
            profession = "Creator",
            ratingsAverage = 4.7f,
            ratingsCount = 0
        ),
        customer = AppointmentUser(
            id = 1,
            fullName = "Radu",
            username = "radu",
            avatar = null,
            profession = "Creator",
            ratingsAverage = 4.7f,
            ratingsCount = 0
        ),
        business = AppointmentBusiness(
            address = "Strada Oarecare",
            coordinates = BusinessCoordinates(
                lat = 45.234f,
                lng = 25.3455f
            ),
            mapUrl = ""
        ),
        totalPrice = BigDecimal(100),
        totalPriceWithDiscount = BigDecimal(100),
        totalDiscount = BigDecimal(0),
        totalDuration = 60,
        paymentCurrency = Currency(
            id = 1,
            name = "RON"
        ),
        hasWrittenReview = false,
        hasVideoReview = false,
        writtenReview = null,
        message = ""
    )
)