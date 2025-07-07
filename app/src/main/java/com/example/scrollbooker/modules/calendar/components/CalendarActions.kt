package com.example.scrollbooker.modules.calendar.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CalendarActions(
    enableBack: Boolean,
    enableNext: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BasePadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(
                enabled = enableBack,
                onClick = onPreviousWeek,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if(enableBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Primary
                )
                Text(
                    text = stringResource(R.string.back),
                    fontWeight = FontWeight.Bold,
                    style = titleMedium,
                    fontSize = 18.sp,
                    color = Primary
                )
            }
        }
        Row(
            modifier = Modifier.clickable(
                enabled = enableNext,
                onClick = onNextWeek
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if(enableNext) {
                Text(
                    text = stringResource(R.string.next),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    style = titleMedium,
                    color = Primary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Primary
                )
            }
        }
    }
}