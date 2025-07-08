package com.example.scrollbooker.modules.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalDate

@Composable
fun CalendarDayTab(
    date: LocalDate,
    onChangeTab: () -> Unit,
    isCurrentTab: Boolean,
    isLoading: Boolean,
    isDayAvailable: Boolean,
    bgColor: Color,
    label: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onChangeTab() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if(isDayAvailable) OnBackground else Divider
            )
            Spacer(Modifier.height(SpacingS))
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = SpacingS),
                    text = date.dayOfMonth.toString(),
                    fontSize = 17.sp,
                    style = titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        isLoading -> Divider
                        !isDayAvailable -> Divider
                        isCurrentTab -> OnPrimary
                        else -> OnBackground
                    }
                )
            }
        }
    }
}