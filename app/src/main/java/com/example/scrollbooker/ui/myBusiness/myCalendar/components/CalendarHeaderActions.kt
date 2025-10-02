package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.myBusiness.myCalendar.durations
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CalendarHeaderActions(
    slotDuration: String,
    isBlocking: Boolean,
    onIsBlocking: () -> Unit,
    onSlotChange: (String?) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = SpacingXS,
            horizontal = BasePadding
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(0.5f)) {
            CalendarDurationSlot(
                label = "Interval",
                options = durations,
                selectedSlot = slotDuration.toString(),
                onSlotChange = onSlotChange
            )
        }

        Spacer(Modifier.width(SpacingS))

        Column(Modifier.weight(0.5f)) {
            MainButton(
                colors = ButtonColors(
                    containerColor = SurfaceBG,
                    contentColor = OnSurfaceBG,
                    disabledContainerColor = Divider,
                    disabledContentColor = OnSurfaceBG
                ),
                contentPadding = PaddingValues(BasePadding),
                shape = ShapeDefaults.Medium,
                title = "Blocheaza sloturi",
                onClick = onIsBlocking
            )
        }
    }
}