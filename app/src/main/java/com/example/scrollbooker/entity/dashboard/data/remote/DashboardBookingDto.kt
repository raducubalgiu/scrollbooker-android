package com.example.scrollbooker.entity.dashboard.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DashboardBookingDto(
    @SerializedName("bookings_no")
    val bookingsNo : Int,

    @SerializedName("finished_bookings_no")
    val finishedBookingsNo: Int,

    @SerializedName("cancelled_bookings_no")
    val cancelledBookingsNo: Int,

    val revenue: BigDecimal,

    @SerializedName("revenue_without_cancelled")
    val revenueWithoutCancellation: BigDecimal,

    @SerializedName("revenue_from_video")
    val revenueFromVideo: BigDecimal,

    val channels: List<DashboardBookingChannelDto>,
    val sources: List<DashboardBookingSourceDto>
)

data class DashboardBookingChannelDto(
    val channel: String,

    @SerializedName("bookings_no")
    val bookingsNo: Int,

    val revenue: BigDecimal,
    val percentage: Float
)

data class DashboardBookingSourceDto(
    val source: String,

    @SerializedName("bookings_no")
    val bookingsNo: Int,

    val revenue: BigDecimal,
    val percentage: Float
)