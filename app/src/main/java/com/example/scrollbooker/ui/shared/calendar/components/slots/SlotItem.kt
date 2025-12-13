package com.example.scrollbooker.ui.shared.calendar.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.extensions.formatHour
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun SlotItem(
    slot: Slot,
    onSelectSlot: (Slot) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .clickable { onSelectSlot(slot) }
            .padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatHour(slot.startDateLocale),
            style = bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if(slot.isLastMinute) {
                Row(
                    modifier = Modifier
                        .clip(shape = ShapeDefaults.Large)
                        .background(LastMinute)
                        .padding(
                            vertical = 2.dp,
                            horizontal = 6.dp
                        )
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last Minute",
                        color = OnPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.width(SpacingXS))

                    Text(
                        text = "-${slot.lastMinuteDiscount}%",
                        color = Error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(SpacingS))
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Primary
            )
        }
    }
}