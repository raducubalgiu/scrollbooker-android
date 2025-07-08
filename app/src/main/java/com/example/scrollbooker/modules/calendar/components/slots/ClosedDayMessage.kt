package com.example.scrollbooker.modules.calendar.components.slots

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ClosedDayMessage(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingXL)
            .padding(top = SpacingXXL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(60.dp),
            painter = painterResource(R.drawable.ic_lock_closed_outline),
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.8f)
        )
        Spacer(Modifier.height(SpacingXL))
        Text(
            text = stringResource(R.string.locationIsClosed),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp
        )
        Spacer(Modifier.height(SpacingS))
        Text(
            text = stringResource(R.string.thisDayDoesntBelongToSchedule),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(SpacingXXL))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainButton(
                title = stringResource(R.string.nextOpenDay),
                onClick = onClick
            )
        }
    }
}