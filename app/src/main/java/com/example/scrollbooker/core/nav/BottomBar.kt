//package com.example.scrollbooker.core.nav
//
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CalendarMonth
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.NavigationBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.unit.dp
//import com.example.scrollbooker.core.nav.routes.MainRoute
//import com.example.scrollbooker.ui.theme.Background
//
//sealed class MainTab(
//    val route: String,
//    val label: String,
//    val iconVector: ImageVector
//) {
//    object Feed: MainTab(MainRoute.Feed.route, "Feed", iconVector = Icons.Default.Home)
//    object Inbox: MainTab(MainRoute.Inbox.route, "Inbox", iconVector = Icons.Default.Notifications)
//    object Search: MainTab(MainRoute.Search.route, "Search", iconVector = Icons.Default.Search)
//    object Appointments: MainTab(MainRoute.Appointments.route, "Appointments", iconVector = Icons.Default.CalendarMonth)
//    object Profile: MainTab(MainRoute.Profile.route, "Profile", iconVector = Icons.Default.Person)
//
//    companion object {
//        fun fromRoute(route: String): MainTab = when(route) {
//            Feed.route -> Feed
//            Inbox.route -> Inbox
//            Search.route -> Search
//            Appointments.route -> Appointments
//            Profile.route -> Profile
//            else -> Feed
//        }
//
//        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
//    }
//}
//
//@Composable
//fun BottomBar(
//    currentTab: MainTab,
//    onTabSelected: (MainTab) -> Unit
//) {
//    val allTabs = MainTab.allTabs
//    val isFeedTab = currentTab == MainTab.Feed
//
//    NavigationBar(
//        modifier = Modifier.fillMaxWidth().height(100.dp),
//        containerColor = if(isFeedTab) Color(0xFF121212) else Background
//    ) {
//        Row(modifier = Modifier
//            .fillMaxSize()
//            .padding(vertical = 10.dp)
//        ) {
//            allTabs.forEach { tab ->
//                BottomBarItem(
//                    modifier = Modifier.then(Modifier.weight(1f)),
//                    onNavigate = { onTabSelected(tab) },
//                    isSelected = currentTab == tab,
//                    tab = tab
//                )
//            }
//        }
//    }
//}