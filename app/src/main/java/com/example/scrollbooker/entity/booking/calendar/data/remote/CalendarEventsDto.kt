package com.example.scrollbooker.entity.booking.calendar.data.remote
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CalendarEventsDto(
    @SerializedName("min_slot_time")
    val minSlotTime: String,

    @SerializedName("max_slot_time")
    val maxSlotTime: String,

    val days: List<CalendarEventsDayDto>
)

data class CalendarEventsDayDto(
    val day: String,

    @SerializedName("is_booked")
    val isBooked: Boolean,

    @SerializedName("is_closed")
    val isClosed: Boolean,

    val slots: List<CalendarEventsSlotDto>
)

data class CalendarEventsSlotDto(
    val id: Int?,

    @SerializedName("start_date_locale")
    val startDateLocale: String,

    @SerializedName("end_date_locale")
    val endDateLocale: String,

    @SerializedName("start_date_utc")
    val startDateUtc: String,

    @SerializedName("end_date_utc")
    val endDateUtc: String,

    @SerializedName("is_booked")
    val isBooked: Boolean,

    @SerializedName("is_closed")
    val isClosed: Boolean,

    @SerializedName("is_blocked")
    val isBlocked: Boolean,

    val info: CalendarEventsInfoDto?
)

data class CalendarEventsCustomerDto(
    val id: Int?,
    val fullname: String,
    val username: String?,
    val avatar: String?
)

data class CalendarEventsInfoDto(
    val currency: Currency?,
    val channel: String,

    @SerializedName("service_name")
    val serviceName: String,
    val product: CalendarEventsProductDto,
    val customer: CalendarEventsCustomerDto?,
    val message: String?
)

data class CalendarEventsProductDto(
    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_full_price")
    val productFullPrice: BigDecimal,

    @SerializedName("product_price_with_discount")
    val productPriceWithDiscount: BigDecimal,

    @SerializedName("product_discount")
    val productDiscount: BigDecimal
)