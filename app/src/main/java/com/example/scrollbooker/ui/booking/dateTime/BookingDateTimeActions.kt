package com.example.scrollbooker.ui.booking.dateTime

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleLarge

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
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (enableBack) handlePreviousWeek() },
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if(enableBack) Divider else Color.Transparent,
                    shape = CircleShape
                ),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (enableBack) OnSurfaceBG.copy(alpha = 0.8f) else Divider
                )
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                )
            }

            Spacer(Modifier.width(SpacingS))

            IconButton(
                onClick = { if (enableNext) handleNextWeek() },
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if(enableNext) Divider else Color.Transparent,
                    shape = CircleShape
                ),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (enableNext) OnSurfaceBG.copy(alpha = 0.8f) else Divider
                )
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                )
            }
        }
    }
}