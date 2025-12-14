package com.example.scrollbooker.entity.booking.calendar.domain.model
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.BusinessShortDomainEnum
import com.example.scrollbooker.core.enums.toDomainColor
import com.example.scrollbooker.core.extensions.monochromeGradient
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

data class SlotUiStyle(
    val background: Brush,
    val lineColor: Color,
    val borderColor: Color
)

data class CalendarEvents(
    val minSlotTime: LocalDateTime?,
    val maxSlotTime: LocalDateTime?,
    val businessShortDomain: BusinessShortDomainEnum,
    val days: List<CalendarEventsDay>
) {
    @Composable
    fun CalendarEventsSlot.resolveUiStyle(): SlotUiStyle {
        val domainColor = businessShortDomain.toDomainColor()

        val baseBg: Color = when {
            isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary
            isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> domainColor
            isBlocked -> Error
            isLastMinute -> LastMinute
            else -> SurfaceBG
        }

        val bgAlpha = when {
            isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> 0.18f
            isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> 0.18f
            isBlocked -> 0.14f
            isLastMinute -> 0.20f
            else -> 1f
        }

        val lineAlpha = when {
            isBooked -> 0.35f
            isBlocked -> 0.35f
            isLastMinute -> 0.35f
            else -> 1f
        }

        val borderAlpha = when {
            isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> 0.25f
            isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> 0.45f
            isBlocked -> 0.45f
            isLastMinute -> 0.45f
            else -> 0.30f
        }

        val (light, dark) = if (isFreeSlot()) 0.04f to 0.02f else 0.10f to 0.06f

        val bgBrush = monochromeGradient(
            base = baseBg.copy(alpha = bgAlpha),
            lightFactor = light,
            darkFactor = dark
        )

        val line = when {
            isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary.copy(alpha = lineAlpha)
            isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> domainColor.copy(alpha = lineAlpha)
            isBlocked -> Error.copy(alpha = lineAlpha)
            isLastMinute -> LastMinute.copy(alpha = lineAlpha)
            else -> SurfaceBG
        }

        val border = when {
            isBooked && info?.channel == AppointmentChannelEnum.SCROLL_BOOKER -> Primary.copy(alpha = borderAlpha)
            isBooked && info?.channel == AppointmentChannelEnum.OWN_CLIENT -> domainColor.copy(alpha = borderAlpha)
            isBlocked -> Error.copy(alpha = borderAlpha)
            isLastMinute -> LastMinute.copy(alpha = borderAlpha)
            else -> Divider.copy(alpha = borderAlpha)
        }

        return SlotUiStyle(
            background = bgBrush,
            lineColor = line,
            borderColor = border
        )
    }
}

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
