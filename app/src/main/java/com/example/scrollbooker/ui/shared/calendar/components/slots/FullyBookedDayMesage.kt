package com.example.scrollbooker.ui.shared.calendar.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun FullyBookedDayMessage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(SurfaceBG, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                tint = OnSurfaceBG,
                painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                contentDescription = null,
            )
        }

        Spacer(Modifier.height(SpacingXL))

        Text(
            text = stringResource(R.string.youArrivedToLate),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp
        )

        Spacer(Modifier.height(SpacingS))

        Text(
            text = stringResource(R.string.emptySets),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(BasePadding))

        MainButtonOutlined(
            title = stringResource(R.string.nextOpenDay),
            onClick = {}
        )
    }
}