package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.myBusiness.myCalendar.durations
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
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
            vertical = SpacingM,
            horizontal = BasePadding
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(0.5f)) {
            CalendarDurationSlot(
                label = stringResource(R.string.interval),
                options = durations,
                selectedSlot = slotDuration.toString(),
                onSlotChange = onSlotChange
            )
        }

        Spacer(Modifier.width(SpacingS))

        Column(Modifier.weight(0.5f)) {
            MainButton(
                colors = ButtonColors(
                    containerColor = if(isBlocking) Error.copy(alpha = 0.2f) else SurfaceBG,
                    contentColor = if(isBlocking) Error else OnSurfaceBG,
                    disabledContainerColor = Divider,
                    disabledContentColor = OnSurfaceBG
                ),
                contentPadding = PaddingValues(BasePadding),
                shape = ShapeDefaults.Medium,
                title = stringResource(R.string.blockSlots),
                onClick = onIsBlocking
            )
        }

        val enableBack = true
        val enableNext = true
        fun handlePreviousWeek() {}
        fun handleNextWeek() {}

        Spacer(Modifier.width(SpacingS))

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

            Spacer(Modifier.width(SpacingXS))

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

//        Spacer(Modifier.width(SpacingS))
//
//        IconButton(
//            onClick = {},
//            colors = IconButtonDefaults.iconButtonColors(
//                containerColor = Beauty,
//                contentColor = OnPrimary
//            )
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = null
//            )
//        }
    }
}