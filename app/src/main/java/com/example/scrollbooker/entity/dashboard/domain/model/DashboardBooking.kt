package com.example.scrollbooker.entity.dashboard.domain.model
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.BookingSourceEnum
import java.math.BigDecimal

data class DashboardBooking(
    val bookingsNo : Int,
    val finishedBookingsNo: Int,
    val cancelledBookingsNo: Int,
    val revenue: BigDecimal,
    val revenueWithoutCancellation: BigDecimal,
    val revenueFromVideo: BigDecimal,
    val channels: List<DashboardBookingChannel>,
    val sources: List<DashboardBookingSource>
)

data class DashboardBookingChannel(
    val channel: AppointmentChannelEnum?,
    val bookingsNo: Int,
    val revenue: BigDecimal,
    val percentage: Float
)

data class DashboardBookingSource(
    val source: BookingSourceEnum?,
    val bookingsNo: Int,
    val revenue: BigDecimal,
    val percentage: Float
)