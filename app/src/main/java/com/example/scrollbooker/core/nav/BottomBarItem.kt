package com.example.scrollbooker.core.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.badge.CustomBadge
import com.example.scrollbooker.core.nav.host.MainTab
import com.example.scrollbooker.core.nav.routes.MainRoute.Appointments
import com.example.scrollbooker.core.nav.routes.MainRoute.Inbox
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    isSelected: Boolean,
    isFeedTab: Boolean,
    tab: MainTab
) {
    val interactionSource = remember { MutableInteractionSource() }

    val contentColor = when {
        isFeedTab && isSelected -> Color(0xFFFDFDFD)
        isSelected -> OnBackground
        else -> Color.Gray
    }

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
                    Inbox.route -> Box(
                        Modifier
                            .offset(x = 10.dp, y = (-7.5).dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .height(9.dp)
                                .clip(CircleShape)
                                .background(Error)
                        )
                    }
                    Appointments.route -> CustomBadge(content = 10)
                }
            }
        ) {
            Box {
                if(tab.route == MainTab.Search.route) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = tab.iconVector,
                        contentDescription = null,
                        tint = if (isSelected) Primary else Color.Gray,
                    )
                } else {
                    Icon(
                        imageVector = tab.iconVector,
                        contentDescription = null,
                        tint = contentColor,
                    )
                }
            }
        }
        Text(
            text = if(tab.route == MainTab.Search.route) "" else tab.label,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}