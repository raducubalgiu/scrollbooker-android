package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
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
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import org.threeten.bp.LocalDateTime

@Composable
fun BlockHeader(
    title: String,
    dayLabel: String,
    selectedSlots: Set<LocalDateTime>
) {
    Text(
        text = title,
        style = titleLarge,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(Modifier.height(SpacingM))

    Text(
        text = dayLabel,
        style = bodyLarge,
        color = Color.Gray
    )

    Spacer(Modifier.height(SpacingM))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        selectedSlots.forEach {
            Row(
                modifier = Modifier
                    .clip(shape = ShapeDefaults.ExtraLarge)
                    .background(SurfaceBG)
                    .padding(vertical = 4.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(17.dp),
                    painter = painterResource(R.drawable.ic_clock_outline),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = parseTimeStringFromLocalDateTimeString(it),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}