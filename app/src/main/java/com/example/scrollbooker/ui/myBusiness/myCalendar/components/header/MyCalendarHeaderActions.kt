package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.dropdown.EmployeeSelectDropdown
import com.example.scrollbooker.components.core.dropdown.OwnIdentityChip
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.HandleNextWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.HandlePreviousWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.OnBlockToggle
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActionsStateAction.OpenEmployeeSheet
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
            vertical = SpacingXS,
            horizontal = BasePadding
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(Modifier.weight(1f)) {
            if (state.hasEmployees) {
                EmployeeSelectDropdown(
                    avatarUrl = state.selectedEmployeeAvatar,
                    name = state.selectedEmployeeName,
                    placeholder = stringResource(R.string.selectEmployee),
                    compact = true,
                    onClick = { onAction(OpenEmployeeSheet) }
                )
            } else {
                OwnIdentityChip(
                    avatarUrl = state.ownAvatar,
                    name = state.ownFullName,
                    compact = true
                )
            }
        }

        Spacer(Modifier.width(SpacingS))

        IconButton(
            onClick = { onAction(OnBlockToggle) },
            enabled = state.hasFreeSlots,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if(state.isBlocking) Error.copy(alpha = 0.2f) else SurfaceBG,
                contentColor = if(state.isBlocking) Error else OnSurfaceBG,
                disabledContainerColor = SurfaceBG,
                disabledContentColor = OnSurfaceBG.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_lock_closed_outline),
                contentDescription = stringResource(R.string.blockSlots),
            )
        }

        Spacer(Modifier.width(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .border(1.dp, Divider, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = state.enableBack) { onAction(HandlePreviousWeek) }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
                    contentDescription = null,
                    tint = if(state.enableBack) OnSurfaceBG.copy(0.8f) else Divider
                )
            }

            Spacer(Modifier.width(SpacingXS))

            Box(
                modifier = Modifier
                    .border(1.dp, Divider, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = state.enableNext) { onAction(HandleNextWeek) }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_chevron_right_outlines),
                    contentDescription = null,
                    tint = if(state.enableNext) OnSurfaceBG.copy(0.8f) else Divider
                )
            }
        }
    }
}
