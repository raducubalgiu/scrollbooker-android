package com.example.scrollbooker.core.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.MainRoute.Appointments
import com.example.scrollbooker.core.nav.routes.MainRoute.Feed
import com.example.scrollbooker.core.nav.routes.MainRoute.Inbox
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(route = "feed", R.string.feed, R.drawable.ic_home),
        BottomNavItem(route = "inbox", R.string.inbox, R.drawable.ic_notifications),
        BottomNavItem(route = "search", R.string.search, R.drawable.ic_search),
        BottomNavItem(route = "appointments", R.string.appointments, R.drawable.ic_calendar),
        BottomNavItem(route = "profile", R.string.profile, R.drawable.ic_person)
    )

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val isFeedScreen = currentRoute == Feed.route

    val selectedColor = if(isFeedScreen) Color(0xFFE0E0E0) else OnBackground
    val containerColor = if(isFeedScreen) Color(0xFF121212) else Background
    val unselectedColor = Color.Gray

    val interactionSource = remember { MutableInteractionSource() }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        tonalElevation = 0.dp,
        containerColor = containerColor
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(vertical = 10.dp)) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route

                Column(modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            if(currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BadgedBox(
                        badge = {
                            when(item.route) {
                                Inbox.route -> Badge(containerColor = Error) { Text(text = "3") }
                                Appointments.route -> Badge(containerColor = Error) { Text(text = "10") }
                            }
                        }
                    ) {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = null,
                            tint = if (isSelected) selectedColor else unselectedColor,
                        )
                    }
                    Text(
                        text = stringResource(id = item.label),
                        color = if (isSelected) selectedColor else unselectedColor,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}