package com.example.scrollbooker.entity.booking.appointment.data.remote
import com.example.scrollbooker.entity.nomenclature.currency.data.remote.CurrencyDto
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentDto(
    val id: Int,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    val channel: String,
    val status: String,
    val message: String?,
    @SerializedName("is_customer")
    val isCustomer: Boolean,

    val products: List<AppointmentProductDto>,

    val user: AppointmentUserDto,
    val customer: AppointmentUserDto,

    val business: AppointmentBusinessDto,

    @SerializedName("total_price")
    val totalPrice: BigDecimal,

    @SerializedName("total_duration")
    val totalDuration: Int,

    @SerializedName("payment_currency")
    val paymentCurrency: CurrencyDto
)

data class AppointmentProductDto(
    val id: Int?,
    val name: String,
    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,

    val duration: Int,

    @SerializedName("currency")
    val currency: CurrencyDto,

    @SerializedName("converted_price_with_discount")
    val convertedPriceWithDiscount: BigDecimal,

    @SerializedName("exchange_rate_used")
    val exchangeRate: BigDecimal?
)

data class AppointmentUserDto(
    val id: Int?,

    @SerializedName("fullname")
    val fullName: String,

    val username: String?,
    val avatar: String?,
    val profession: String?
)

data class BusinessCoordinatesDto(
    val lat: Float,
    val lng: Float
)

data class AppointmentBusinessDto(
    val address: String,
    val coordinates: BusinessCoordinatesDto,
    val mapUrl: String?
)