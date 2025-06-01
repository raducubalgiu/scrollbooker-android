package com.example.scrollbooker.core.nav
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.host.currentRoute
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.ui.theme.Background

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(route = "feed", R.string.feed, R.drawable.ic_home),
        BottomNavItem(route = "inbox", R.string.inbox, R.drawable.ic_notifications),
        BottomNavItem(route = "search", R.string.search, R.drawable.ic_search),
        BottomNavItem(route = "appointments", R.string.appointments, R.drawable.ic_calendar),
        BottomNavItem(route = "profile", R.string.profile, R.drawable.ic_person)
    )

    val currentRoute = currentRoute(navController = navController)
    val containerColor = if(currentRoute == MainRoute.Feed.route) Color(0xFF121212) else Background

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        containerColor = containerColor
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
        ) {
            items.forEach { tab ->
                val isSelected = currentRoute == tab.route

                BottomBarItem(
                    modifier = Modifier.then(Modifier.weight(1f)),
                    onNavigate = {
                        if(currentRoute != tab.route) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    isSelected = isSelected,
                    tab = tab
                )
            }
        }
    }
}