package com.example.scrollbooker.ui.myBusiness.myCalendar

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.extensions.parseDateTimeStringToLocalDateTime
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsCustomer
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsInfo
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsProduct
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import java.math.BigDecimal

val calendarMockData: CalendarEvents = CalendarEvents(
    minSlotTime = parseDateTimeStringToLocalDateTime("2025-10-06T09:00:00"),
    maxSlotTime = parseDateTimeStringToLocalDateTime("2025-10-06T09:00:00"),
    days = listOf(
        CalendarEventsDay(
            day = "2025-10-06",
            isBooked = false,
            isClosed = false,
            slots = listOf(
                CalendarEventsSlot(
                    id = null,
                    startDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T09:00:00"),
                    endDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T10:00:00"),
                    startDateUtc = "2025-10-06T06:00:00+00:00",
                    endDateUtc = "2025-10-06T07:00:00+00:00",
                    isBooked = false,
                    isClosed = false,
                    isBlocked = false,
                    info = null
                ),
                CalendarEventsSlot(
                    id = 1,
                    startDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T10:00:00"),
                    endDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T11:00:00"),
                    startDateUtc = "2025-10-06T07:00:00+00:00",
                    endDateUtc = "2025-10-06T08:00:00+00:00",
                    isBooked = true,
                    isClosed = false,
                    isBlocked = false,
                    info = CalendarEventsInfo(
                        currency = Currency(
                            id = 1,
                            name = "RON"
                        ),
                        channel = AppointmentChannelEnum.SCROLL_BOOKER,
                        serviceName = "Tuns",
                        product = CalendarEventsProduct(
                            productName = "Tuns special",
                            productFullPrice = BigDecimal("100.0"),
                            productPriceWithDiscount = BigDecimal("100.0"),
                            productDiscount = BigDecimal("0.0")
                        ),
                        customer = CalendarEventsCustomer(
                            id = 1,
                            fullname = "Raducu Balgiu",
                            username = "radu_balgiu",
                            avatar = "",
                        ),
                        message = null
                    )
                ),
                CalendarEventsSlot(
                    id = null,
                    startDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T11:00:00"),
                    endDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T12:00:00"),
                    startDateUtc = "2025-10-06T08:00:00+00:00",
                    endDateUtc = "2025-10-06T09:00:00+00:00",
                    isBooked = false,
                    isClosed = false,
                    isBlocked = false,
                    info = null
                ),
                CalendarEventsSlot(
                    id = null,
                    startDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T12:00:00"),
                    endDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T13:00:00"),
                    startDateUtc = "2025-10-07T09:00:00+00:00",
                    endDateUtc = "2025-10-08T10:00:00+00:00",
                    isBooked = false,
                    isClosed = false,
                    isBlocked = true,
                    info = CalendarEventsInfo(
                        currency = null,
                        channel = AppointmentChannelEnum.OWN_CLIENT,
                        serviceName = "Blocked",
                        product = CalendarEventsProduct(
                            productName = "Blocked",
                            productFullPrice = BigDecimal("0.0"),
                            productPriceWithDiscount = BigDecimal("0.0"),
                            productDiscount = BigDecimal("0.0")
                        ),
                        customer = null,
                        message = "Pauza de masa"
                    )
                ),
                CalendarEventsSlot(
                    id = null,
                    startDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T13:00:00"),
                    endDateLocale = parseDateTimeStringToLocalDateTime("2025-10-06T14:00:00"),
                    startDateUtc = "2025-10-07T08:00:00+00:00",
                    endDateUtc = "2025-10-08T09:00:00+00:00",
                    isBooked = true,
                    isClosed = false,
                    isBlocked = false,
                    info = CalendarEventsInfo(
                        currency = null,
                        channel = AppointmentChannelEnum.OWN_CLIENT,
                        serviceName = "Tuns",
                        product = CalendarEventsProduct(
                            productName = "Tuns",
                            productFullPrice = BigDecimal("120.0"),
                            productPriceWithDiscount = BigDecimal("120.0"),
                            productDiscount = BigDecimal("0.0")
                        ),
                        customer = CalendarEventsCustomer(
                            id = null,
                            fullname = "Gigio Donarumma",
                            username = "gigi",
                            avatar = "",
                        ),
                        message = null
                    )
                ),
            )
        )
    )
)