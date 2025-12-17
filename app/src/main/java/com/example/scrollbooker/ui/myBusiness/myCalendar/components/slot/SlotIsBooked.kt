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
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun SlotIsBooked(
    slot: CalendarEventsSlot,
    maxLines: Int
) {
    val title = slot.info?.customer?.fullname ?: stringResource(R.string.booked)

    Column {
        Text(
            text = title,
            style = bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(SpacingM))

        Text(
            text = "Tuns Special • ${slot.info?.totalPriceWithDiscount}",
            style = bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}