package com.example.scrollbooker.ui.shared.calendar.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(35.dp),
                painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.width(SpacingM))
            Text(
                style = titleMedium,
                text = period,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (enableBack) handlePreviousWeek() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if(enableBack) SurfaceBG else Color.Transparent,
                    contentColor = if(enableBack) OnSurfaceBG.copy(0.8f) else Divider
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
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if(enableNext) SurfaceBG else Color.Transparent,
                    contentColor = if(enableNext) OnSurfaceBG.copy(0.8f) else Divider
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