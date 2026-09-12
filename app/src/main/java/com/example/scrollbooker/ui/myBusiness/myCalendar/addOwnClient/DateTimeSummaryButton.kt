package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun DateTimeSummaryButton(
    value: String?,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.ExtraLarge)
            .background(SurfaceBG)
            .border(1.dp, Divider, ShapeDefaults.ExtraLarge)
            .clickable(enabled = enabled) { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = value ?: stringResource(
                if (enabled) R.string.selectDateAndTime else R.string.selectServicesFirst
            ),
            style = bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (value != null) OnSurfaceBG else OnSurfaceBG.copy(alpha = 0.5f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
            contentDescription = null,
            tint = OnSurfaceBG.copy(alpha = if (enabled) 0.6f else 0.3f)
        )
    }
}
