package com.example.scrollbooker.modules.calendar.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.formatHour
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
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
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Primary
        )
    }
}