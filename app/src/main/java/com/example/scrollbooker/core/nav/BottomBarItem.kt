package com.example.scrollbooker.core.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.nav.host.MainTab
import com.example.scrollbooker.core.nav.routes.MainRoute.Appointments
import com.example.scrollbooker.core.nav.routes.MainRoute.Inbox
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    isSelected: Boolean,
    tab: MainTab
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onNavigate
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BadgedBox(
            badge = {
                when(tab.route) {
                    Inbox.route -> Badge(containerColor = Error) { Text(text = "3") }
                    Appointments.route -> Badge(containerColor = Error) { Text(text = "10") }
                }
            }
        ) {
            Icon(
                imageVector = tab.iconVector,
                contentDescription = null,
                tint = if (isSelected) OnBackground else Color.Gray,
            )
        }
        Text(
            text = tab.label,
            color = if (isSelected) OnBackground else Color.Gray,
            fontSize = 12.sp
        )
    }
}