package com.example.scrollbooker.core.nav

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
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
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.routes.MainRoute.Appointments
import com.example.scrollbooker.core.nav.routes.MainRoute.Feed
import com.example.scrollbooker.core.nav.routes.MainRoute.Inbox
import com.example.scrollbooker.core.nav.routes.MainRoute.Profile
import com.example.scrollbooker.core.nav.routes.MainRoute.Search

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Feed, Inbox, Search, Appointments, Profile)
    val error = MaterialTheme.colorScheme.error
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val unselectedColor = Color.Gray
    val selectedColor = if(currentRoute == Feed.route) Color(0xFFE0E0E0) else onBackground
    val containerColor = if(currentRoute == Feed.route) Color(0xFF121212) else background


    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 0.dp,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if(currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                        Icon(painterResource(id = item.icon),
                            contentDescription = null,
                            tint = if (isSelected) selectedColor else unselectedColor
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
