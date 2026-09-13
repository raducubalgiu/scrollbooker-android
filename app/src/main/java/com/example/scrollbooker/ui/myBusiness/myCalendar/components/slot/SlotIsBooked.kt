package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun SlotIsBooked(
    slot: CalendarEventsSlot,
    maxLines: Int
) {
    val title = slot.info?.customer?.fullname ?: stringResource(R.string.booked)

    val servicesNames = slot.info?.products
        ?.joinToString(" • ") { it.productName }
        .orEmpty()

    val price = slot.info?.totalPriceWithDiscount?.toTwoDecimals()
    val currencyName = slot.info?.paymentCurrency?.name

    val subtitle = listOfNotNull(
        servicesNames.takeIf { it.isNotBlank() },
        price?.let { if (currencyName != null) "$it $currencyName" else it }
    ).joinToString(" • ")

    Column {
        Text(
            text = title,
            style = bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(SpacingM))

        Text(
            text = subtitle,
            style = bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}