package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

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
import androidx.compose.material3.ButtonDefaults
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
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.HandleNextWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.HandlePreviousWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.OnBlockToggle
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.OnSlotChange
import com.example.scrollbooker.ui.myBusiness.myCalendar.durations
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun MyCalendarHeaderActions(
    state: MyCalendarHeaderActionsState,
    onAction: (MyCalendarHeaderActionsStateAction) -> Unit
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
            MyCalendarDurationAction(
                label = stringResource(R.string.interval),
                options = durations,
                selectedSlot = state.slotDuration.toString(),
                onSlotChange = { onAction(OnSlotChange(it)) }
            )
        }

        Spacer(Modifier.width(SpacingS))

        Column(Modifier.weight(0.5f)) {
            MainButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(state.isBlocking) Error.copy(alpha = 0.2f) else SurfaceBG,
                    contentColor = if(state.isBlocking) Error else OnSurfaceBG,
                ),
                contentPadding = PaddingValues(BasePadding),
                shape = ShapeDefaults.Medium,
                title = stringResource(R.string.blockSlots),
                onClick = { onAction(OnBlockToggle) },
                enabled = state.hasFreeSlots
            )
        }

        Spacer(Modifier.width(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (state.enableBack) onAction(HandlePreviousWeek) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if(state.enableBack) SurfaceBG else Color.Transparent,
                    contentColor = if(state.enableBack) OnSurfaceBG.copy(0.8f) else Divider
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
                onClick = { if (state.enableNext) onAction(HandleNextWeek) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if(state.enableNext) SurfaceBG else Color.Transparent,
                    contentColor = if(state.enableNext) OnSurfaceBG.copy(0.8f) else Divider
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