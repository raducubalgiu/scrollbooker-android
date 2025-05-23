package com.example.scrollbooker.core.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = containerColor
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if(currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
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
                },
                label = { Text(stringResource(id = item.label)) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor
                ),
                interactionSource = remember { MutableInteractionSource() },
                enabled = true,
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
)