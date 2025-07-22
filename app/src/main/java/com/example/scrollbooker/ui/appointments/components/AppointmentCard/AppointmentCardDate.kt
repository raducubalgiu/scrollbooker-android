package com.example.scrollbooker.ui.appointments.components.AppointmentCard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun AppointmentCardDate(
    day: String,
    month: String,
    startTime: String
) {
    Column(
        modifier = Modifier
            .border(
                width = 0.55.dp,
                color = Divider,
                shape = ShapeDefaults.Large
            )

    ) {
        Column(
            modifier = Modifier.padding(
                vertical = SpacingM,
                horizontal = BasePadding
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day,
                style = headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(vertical = SpacingXS),
                text = month.replaceFirstChar { it.uppercase() },
                color = Color.Gray,
                style = titleSmall,
            )
            Text(
                text = startTime,
                fontWeight = FontWeight.Bold
            )
        }
    }
}