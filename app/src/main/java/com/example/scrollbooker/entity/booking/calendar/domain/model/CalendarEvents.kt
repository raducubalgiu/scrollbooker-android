package com.example.scrollbooker.entity.booking.calendar.domain.model
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.ui.theme.Beauty
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

data class CalendarEvents(
    val minSlotTime: LocalDateTime?,
    val maxSlotTime: LocalDateTime?,
    val days: List<CalendarEventsDay>
)

data class CalendarEventsDay(
    val day: String,
    val isBooked: Boolean,
    val isClosed: Boolean,
    val slots: List<CalendarEventsSlot>
)

data class CalendarEventsSlot(
    val id: Int?,
    val startDateLocale: LocalDateTime?,
    val endDateLocale: LocalDateTime?,
    val startDateUtc: String,
    val endDateUtc: String,
    val isBooked: Boolean,
    val isBlocked: Boolean,
    val isLastMinute: Boolean,
    val lastMinuteDiscount: BigDecimal?,
    val info: CalendarEventsInfo?
)

data class CalendarEventsCustomer(
    val id: Int?,
    val fullname: String,
    val username: String?,
    val avatar: String?
)

data class CalendarEventsInfo(
    val channel: AppointmentChannelEnum?,
    val customer: CalendarEventsCustomer?,
    val message: String?,
    val totalPrice: BigDecimal,
    val totalPriceWithDiscount: BigDecimal,
    val totalDiscount: BigDecimal,
    val totalDuration: Int,
    val paymentCurrency: Currency,
)

fun CalendarEvents.blockedStartLocale(): Set<LocalDateTime> =
    days.asSequence()
        .flatMap { it.slots.asSequence() }
        .filter { it.isBlocked }
        .map { it.startDateLocale }
        .filterNotNull()
        .toSet()

data class SlotTimeBounds(
    val start: String,
    val end: String
)

fun CalendarEventsSlot.toTime(): SlotTimeBounds =
    SlotTimeBounds(
        start = parseTimeStringFromLocalDateTimeString(startDateLocale),
        end = parseTimeStringFromLocalDateTimeString(endDateLocale)
    )

fun CalendarEventsSlot.isFreeSlot(): Boolean =
    !isBooked && !isBlocked && !isLastMinute

@Composable
fun CalendarEventsSlot.getBgColor(): Brush {
    val base: Color = when {
        isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary.copy(alpha = 0.18f)
        isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> Beauty.copy(alpha = 0.18f)
        isBlocked -> Error.copy(alpha = 0.14f)
        isLastMinute -> LastMinute.copy(alpha = 0.20f)
        else -> SurfaceBG
    }

    val (light, dark) = if(isFreeSlot()) 0.04f to 0.02f else 0.1f to 0.06f

    return monochromeGradient(
        base = base,
        lightFactor = light,
        darkFactor = dark
    )
}

@Composable
fun CalendarEventsSlot.getLineColor(): Color =
    when {
        isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary.copy(alpha = 0.35f)
        isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> Beauty.copy(alpha = 0.35f)
        isBlocked -> Error.copy(alpha = 0.35f)
        isLastMinute -> LastMinute.copy(alpha = 0.35f)
        else -> SurfaceBG
    }

@Composable
fun CalendarEventsSlot.getBorderColor(): Color =
    when {
        isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary.copy(alpha = 0.25f)
        isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> Beauty.copy(alpha = 0.45f)
        isBlocked -> Error.copy(alpha = 0.45f)
        isLastMinute -> LastMinute.copy(alpha = 0.45f)
        else -> Divider.copy(alpha = 0.3f)
    }

fun monochromeGradient(
    base: Color,
    lightFactor: Float = 0.12f,
    darkFactor: Float = 0.06f
): Brush {
    val lighter = lerp(base, Color.White, lightFactor)
    val darker = lerp(base, Color.Black, darkFactor)

    return Brush.linearGradient(
        colors = listOf(lighter, darker),
        start = Offset.Zero,
        end = Offset.Infinite
    )
}