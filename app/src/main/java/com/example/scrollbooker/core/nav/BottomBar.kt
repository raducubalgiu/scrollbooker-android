package com.example.scrollbooker.core.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(route = "feed", R.string.feed, R.drawable.ic_home),
        BottomNavItem(route = "inbox", R.string.inbox, R.drawable.ic_notifications),
        BottomNavItem(route = "search", R.string.search, R.drawable.ic_search),
        BottomNavItem(route = "appointments", R.string.appointments, R.drawable.ic_calendar),
        BottomNavItem(route = "profile", R.string.profile, R.drawable.ic_person)
    )

    val error = MaterialTheme.colorScheme.error
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val unselectedColor = Color.Gray
    val selectedColor = if(currentRoute == Feed.route) Color(0xFFE0E0E0) else onBackground
    val containerColor = if(currentRoute == Feed.route) Color(0xFF121212) else background

    val interactionSource = remember { MutableInteractionSource() }

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = containerColor
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
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
                    )
                    .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BadgedBox(
                        badge = {
                            when(item.route) {
                                Inbox.route -> Badge(containerColor = error) { Text(text = "3") }
                                Appointments.route -> Badge(containerColor = error) { Text(text = "10") }
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