package com.example.scrollbooker.entity.booking.appointment.domain.model
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.CANCELED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.FINISHED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.IN_PROGRESS
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal

data class Appointment(
    val id: Int,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val channel: AppointmentChannelEnum?,
    val status: AppointmentStatusEnum?,
    val message: String?,
    val isCustomer: Boolean,
    val products: List<AppointmentProduct>,
    val user: AppointmentUser,
    val customer: AppointmentUser,
    val business: AppointmentBusiness,
    val totalPrice: BigDecimal,
    val totalPriceWithDiscount: BigDecimal,
    val totalDiscount: BigDecimal,
    val totalDuration: Int,
    val paymentCurrency: Currency,
    val hasWrittenReview: Boolean,
    val hasVideoReview: Boolean,
    val writtenReview: AppointmentWrittenReview?
)

data class AppointmentWrittenReview(
    val id: Int,
    val review: String?,
    val rating: Int
)

data class AppointmentProduct(
    val id: Int?,
    val name: String,
    val productVariantId: Int?,
    val offeringId: Int?,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val duration: Int,
    val currency: Currency,
    val convertedPriceWithDiscount: BigDecimal,
    val exchangeRate: BigDecimal?,
    val filters: List<ProductFilter>
)

data class AppointmentUser(
    val id: Int?,
    val fullName: String,
    val username: String?,
    val avatar: String?,
    val profession: String?,
    val ratingsAverage: Float?,
    val ratingsCount: Int?
)

data class BusinessCoordinates(
    val lat: Float,
    val lng: Float
)

data class AppointmentBusiness(
    val id: Int,
    val businessOwnerId: Int,
    val address: String,
    val coordinates: BusinessCoordinates,
    val mapUrl: String?
)



fun Appointment.getProductNames(): String =
    products.joinToString(", ") { it.name }

fun AppointmentProduct.getDurationText(): String {
    if (duration == 0) return "0min"

    val hours = duration / 60
    val remainingMinutes = duration % 60

    val hoursPart = if (hours > 0) "${hours}h" else ""
    val minutesPart = if (remainingMinutes > 0) "${remainingMinutes}min" else ""

    return listOf(hoursPart, minutesPart)
        .filter { it.isNotEmpty() }
        .joinToString(" ")
}

fun AppointmentProduct.getFiltersSummary(): String {
    val filterParts = this.filters.mapNotNull { filter ->
        if (filter.subFilters.isEmpty()) null
        else filter.subFilters.joinToString(" & ") { it.name }
    }

    return filterParts.joinToString(" • ")
}

@StringRes
fun Appointment.getStatusRes(): Int =
    when(status) {
        IN_PROGRESS -> R.string.confirmed
        FINISHED -> R.string.finished
        CANCELED -> R.string.canceled
        null -> R.string.unknown
    }

fun Appointment.getStatusColor(): Color =
    when(status) {
        IN_PROGRESS -> Color(0xFF66BB6A)
        FINISHED -> Color.Gray
        CANCELED -> Color.Red
        null -> Color.Gray
    }


