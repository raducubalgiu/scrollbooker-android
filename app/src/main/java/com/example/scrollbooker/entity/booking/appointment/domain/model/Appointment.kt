package com.example.scrollbooker.entity.booking.appointment.domain.model
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.CANCELED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.FINISHED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.IN_PROGRESS
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.math.BigDecimal
import java.util.Locale

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
)

data class AppointmentProduct(
    val id: Int?,
    val name: String,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val duration: Int,
    val currency: Currency,
    val convertedPriceWithDiscount: BigDecimal,
    val exchangeRate: BigDecimal?
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
    val address: String,
    val coordinates: BusinessCoordinates,
    val mapUrl: String?
)

private val DAY_FMT = DateTimeFormatter.ofPattern("dd")
private fun monthFmt(locale: Locale) = DateTimeFormatter.ofPattern("MMM", locale)
private val TIME_FMT = DateTimeFormatter.ofPattern("HH:mm")

fun Appointment.localStart(zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime =
    startDate.withZoneSameInstant(zone)

fun Appointment.getDay(zone: ZoneId = ZoneId.systemDefault()): String =
    localStart(zone).format(DAY_FMT)

fun Appointment.getMonth(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current()
): String =
    localStart(zone).format(monthFmt(locale)).replaceFirstChar { it.uppercase() }

fun Appointment.getTime(zone: ZoneId = ZoneId.systemDefault()): String =
    localStart(zone).format(TIME_FMT)

fun Appointment.displayAppointmentDate(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current()
): String {
    val dt = localStart(zone)

    val dayOfWeek = dt.format(DateTimeFormatter.ofPattern("EEE", locale))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

    val day = dt.format(DateTimeFormatter.ofPattern("d", locale))

    val month = dt.format(monthFmt(locale))
        .lowercase()
        .removeSuffix(".")

    val time = dt.format(TIME_FMT)

    return "$dayOfWeek, $day $month, $time"
}

fun Appointment.getProductNames(): String =
    products.joinToString(", ") { it.name }

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


