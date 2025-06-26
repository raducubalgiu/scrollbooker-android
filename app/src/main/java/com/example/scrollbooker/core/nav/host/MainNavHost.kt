package com.example.scrollbooker.core.nav.host
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.BottomBarItem
import com.example.scrollbooker.core.nav.containers.DefaultTabContainer
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.feed.FeedScreen
import com.example.scrollbooker.screens.feed.FeedViewModel
import com.example.scrollbooker.screens.profile.myProfile.MyProfileScreen
import com.example.scrollbooker.screens.profile.myProfile.ProfileSharedViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class MainTab(
    val route: String,
    val label: String,
    val painter: Int
) {
    object Feed: MainTab(
        MainRoute.Feed.route, "Acasa",
        painter = R.drawable.ic_home_solid
    )
    object Inbox: MainTab(
        MainRoute.Inbox.route, "Inbox",
        painter = R.drawable.ic_notifications_solid
    )
    object Search: MainTab(
        MainRoute.Search.route, "Search",
        painter = R.drawable.ic_search_solid
    )
    object Appointments: MainTab(
        MainRoute.Appointments.route, "Comenzi",
        painter = R.drawable.ic_clipboard_solid
    )
    object Profile: MainTab(
        MainRoute.MyProfile.route, "Profil",
        painter = R.drawable.ic_person_solid
    )

    companion object {
        fun fromRoute(route: String): MainTab = when(route) {
            Feed.route -> Feed
            Inbox.route -> Inbox
            Search.route -> Search
            Appointments.route -> Appointments
            Profile.route -> Profile
            else -> Feed
        }

        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}


val MainTabSaver: Saver<MainTab, String> = Saver(
    save = { it.route },
    restore = { route -> MainTab.fromRoute(route) }
)

@Composable
fun MainNavHost() {
    val allTabs = MainTab.allTabs

    var currentTab by rememberSaveable(stateSaver = MainTabSaver) {
        mutableStateOf(MainTab.Feed)
    }

    val navControllers = remember {
        mutableMapOf<MainTab, NavHostController>()
    }.also { controllers ->
        MainTab.allTabs.forEach { tab ->
            controllers.putIfAbsent(tab, rememberNavController())
        }
    }

    val saveableStateHolder = rememberSaveableStateHolder()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isFeedTab = currentTab == MainTab.Feed
    val bottomBarRoutes = MainTab.allTabs.map { it.route }

    val currentNavController = navControllers[currentTab]!!
    val currentBackStackEntry by currentNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val dividerColor = if (isFeedTab) Color(0xFF3A3A3A) else Divider
    val containerColor = if(isFeedTab) Color(0xFF121212) else Background

    val scaffoldContent: @Composable () -> Unit = {
        Scaffold(
            bottomBar = {
                if(currentRoute in bottomBarRoutes) {
                    Column(Modifier.height(90.dp)) {
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        NavigationBar(
                            tonalElevation = 0.dp,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = containerColor
                        ) {
                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 5.dp)
                            ) {
                                allTabs.forEach { tab ->
                                    BottomBarItem(
                                        modifier = Modifier.then(Modifier.weight(1f)),
                                        onNavigate = { currentTab = tab },
                                        isSelected = currentTab == tab,
                                        isFeedTab = isFeedTab,
                                        tab = tab
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                saveableStateHolder.SaveableStateProvider(currentTab.route) {
                    when (currentTab) {
                        is MainTab.Feed -> {
                            FeedNavHost(
                                navController = navControllers[MainTab.Feed]!!,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )
                        }

                        is MainTab.Inbox -> {
                            DefaultTabContainer(
                                navController = navControllers[MainTab.Inbox]!!,
                                innerPadding = innerPadding,
                                content = { InboxNavHost(navController = it) }
                            )
                        }
                        is MainTab.Search -> SearchNavHost(navController = navControllers[MainTab.Search]!!)
                        is MainTab.Appointments -> {
                            DefaultTabContainer(
                                navController = navControllers[MainTab.Appointments]!!,
                                innerPadding = innerPadding,
                                content = { AppointmentsNavHost(navController = it) }
                            )
                        }
                        is MainTab.Profile -> {
                            DefaultTabContainer(
                                enablePadding = false,
                                navController = navControllers[MainTab.Profile]!!,
                                innerPadding = innerPadding,
                                content = { ProfileNavHost(navController = it) }
                            )
                        }
                    }
                }
            }
        }
    }

    if(isFeedTab) {
        ModalNavigationDrawer(
            drawerContent = { AppDrawer() },
            scrimColor = Color(0xFF121212).copy(0.7f),
            drawerState = drawerState,
            gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        ) { scaffoldContent() }
    } else {
        scaffoldContent()
    }
}
