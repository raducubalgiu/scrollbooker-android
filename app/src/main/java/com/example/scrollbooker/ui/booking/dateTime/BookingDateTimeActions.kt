package com.example.scrollbooker.ui.booking.dateTime

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BookingDateTimeActions(
    period: String,
    enableBack: Boolean,
    enableNext: Boolean,
    handlePreviousWeek: () -> Unit,
    handleNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(30.dp),
                tint = Color.Gray,
                painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                contentDescription = null
            )

            Spacer(Modifier.width(SpacingS))

            Text(
                text = period,
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .border(1.dp, Divider, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = enableBack) { handlePreviousWeek() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
                    contentDescription = null,
                    tint = if(enableBack) OnSurfaceBG.copy(0.8f) else Divider
                )
            }

            Spacer(Modifier.width(SpacingXS))

            Box(
                modifier = Modifier
                    .border(1.dp, Divider, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = enableNext) { handleNextWeek() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_chevron_right_outlines),
                    contentDescription = null,
                    tint = if(enableNext) OnSurfaceBG.copy(0.8f) else Divider
                )
            }
        }
    }
}