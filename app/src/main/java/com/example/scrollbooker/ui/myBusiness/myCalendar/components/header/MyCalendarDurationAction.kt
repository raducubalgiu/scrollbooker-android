package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myCalendar.durations
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge
import androidx.compose.material3.Text

@Composable
fun MyCalendarDurationAction(
    label: String,
    selectedSlot: String,
    onClick: () -> Unit,
) {
    val selected = durations.find { it.value == selectedSlot }

    Row(
        modifier = Modifier
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .clickable(onClick = onClick)
            .padding(
                horizontal = BasePadding,
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = labelLarge,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = selected?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = OnSurfaceBG,
                fontWeight = FontWeight.SemiBold
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
        )
    }
}
