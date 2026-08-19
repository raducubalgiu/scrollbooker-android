package com.example.scrollbooker.entity.dashboard.data.mapper

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.dashboard.data.remote.DashboardBookingChannelDto
import com.example.scrollbooker.entity.dashboard.data.remote.DashboardBookingDto
import com.example.scrollbooker.entity.dashboard.data.remote.DashboardBookingSourceDto
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBookingChannel
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBookingSource

fun DashboardBookingDto.toDomain(): DashboardBooking {
    return DashboardBooking(
        bookingsNo = this.bookingsNo,
        finishedBookingsNo = this.finishedBookingsNo,
        cancelledBookingsNo = this.cancelledBookingsNo,
        revenue = this.revenue,
        revenueWithoutCancellation = this.revenueWithoutCancellation,
        revenueFromVideo = this.revenueFromVideo,
        channels = this.channels.map { it.toDomain() },
        sources = this.sources.map { it.toDomain() }
    )
}

fun DashboardBookingChannelDto.toDomain(): DashboardBookingChannel {
    return DashboardBookingChannel(
        channel = AppointmentChannelEnum.fromKey(this.channel),
        bookingsNo = this.bookingsNo,
        revenue = this.revenue,
        percentage = this.percentage
    )
}

fun DashboardBookingSourceDto.toDomain(): DashboardBookingSource {
    return DashboardBookingSource(
        source = BookingSourceEnum.fromKey(this.source),
        bookingsNo = this.bookingsNo,
        revenue = this.revenue,
        percentage = this.percentage
    )
}