package com.example.scrollbooker.modules.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CalendarHeader(
    period: String,
    enableBack: Boolean,
    enableNext: Boolean,
    handlePreviousWeek: () -> Unit,
    handleNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = SpacingXL,
                vertical = BasePadding
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.width(SpacingM))
            Text(
                style = titleMedium,
                fontSize = 17.sp,
                text = period,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .size(42.5.dp)
                    .shadow(
                        elevation = if(enableBack) 1.dp else 0.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .clip(CircleShape)
                    .background(
                        color = if(enableBack) Color(0xFFE8E8E8) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { handlePreviousWeek() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
                    contentDescription = null,
                    tint = if(enableBack) OnSurfaceBG.copy(0.8f) else Divider
                )
            }

            Spacer(Modifier.width(SpacingM))

            Column(
                modifier = Modifier
                    .size(42.5.dp)
                    .shadow(
                        elevation = if(enableNext) 1.dp else 0.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .clip(CircleShape)
                    .background(
                        color = if(enableNext) Color(0xFFE8E8E8) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { handleNextWeek() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_arrow_chevron_right_outlines),
                    contentDescription = null,
                    tint = if(enableNext) OnSurfaceBG.copy(0.8f) else Divider
                )
            }
        }
    }
}