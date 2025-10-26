package com.example.scrollbooker.navigation.bottomBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.badge.BulletBadge
import com.example.scrollbooker.components.core.badge.CustomBadge
import com.example.scrollbooker.navigation.bottomBar.MainTab.Appointments
import com.example.scrollbooker.navigation.bottomBar.MainTab.Inbox
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun BottomBarItem(
    appointmentsNumber: Int,
    notificationsNumber: Int,
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

    val painter = if(isSelected) tab.painterSolid else tab.painterOutline

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
                    Inbox.route -> {
                        if(notificationsNumber > 0) {
                            BulletBadge()
                        }
                    }
                    Appointments.route -> {
                        if(appointmentsNumber > 0) {
                            CustomBadge(content = appointmentsNumber)
                        }
                    }
                }
            }
        ) {
            Box {
                Icon(
                    modifier = Modifier.size(27.5.dp),
                    painter = painterResource(painter),
                    contentDescription = null,
                    tint = contentColor,
                )
            }
        }
        Text(
            text = tab.label,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}