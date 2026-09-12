package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectServices
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.productCard.ProductCardRowPrice
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

private fun formatDurationText(minutes: Int): String {
    if (minutes <= 0) return "0min"

    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    val hoursPart = if (hours > 0) "${hours}h" else ""
    val minutesPart = if (remainingMinutes > 0) "${remainingMinutes}min" else ""

    return listOf(hoursPart, minutesPart)
        .filter { it.isNotEmpty() }
        .joinToString(" ")
}

@Composable
fun LinkedServiceItemRow(
    modifier: Modifier = Modifier,
    item: SelectedBookingItem,
    onRemove: (SelectedBookingItem) -> Unit
) {
    val offering = item.offerings.firstOrNull()

    val durationAndFilters = remember(item) {
        val durationText = formatDurationText(item.variantDuration)
        if (item.filtersSummary.isBlank()) durationText else "$durationText - ${item.filtersSummary}"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.productName,
                style = titleMedium,
                color = OnBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(SpacingXXS))

            Text(
                text = durationAndFilters,
                style = bodyMedium,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(SpacingS))

            ProductCardRowPrice(
                hasDifferentOfferings = false,
                price = offering?.price ?: BigDecimal.ZERO,
                priceWithDiscount = offering?.priceWithDiscount ?: BigDecimal.ZERO,
                discount = offering?.discount ?: BigDecimal.ZERO
            )
        }

        Spacer(Modifier.width(SpacingS))

        IconButton(onClick = { onRemove(item) }) {
            Icon(
                painter = painterResource(R.drawable.ic_delete_outline),
                contentDescription = null,
                tint = Error
            )
        }
    }
}
