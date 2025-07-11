package com.example.scrollbooker.core.nav.host
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.bottomBar.MainTab
import com.example.scrollbooker.core.nav.containers.DefaultTabContainer
import com.example.scrollbooker.screens.auth.AuthViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

val MainTabSaver: Saver<MainTab, String> = Saver(
    save = { it.route },
    restore = { route -> MainTab.fromRoute(route) }
)

@Composable
fun MainNavHost(authViewModel: AuthViewModel) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navControllers = remember {
        mutableMapOf<MainTab, NavHostController>()
    }.also { controllers ->
        MainTab.allTabs.forEach { tab ->
            controllers.putIfAbsent(tab, rememberNavController())
        }
    }

    var currentTab by rememberSaveable(stateSaver = MainTabSaver) {
        mutableStateOf(MainTab.Feed)
    }

    val currentNavController = checkNotNull(navControllers[currentTab]) {
        Timber.tag("Current NavController").e("NavController for tab $currentTab is not initialized")
    }

    val currentBackStackEntry by currentNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(
                drawerState = drawerState,
                currentTab = currentTab,
                currentRoute = currentRoute,
                onNavigate = { currentTab = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            saveableStateHolder.SaveableStateProvider(currentTab.route) {
                when (currentTab) {
                    is MainTab.Feed -> {
                        ModalNavigationDrawer(
                            drawerContent = { AppDrawer() },
                            scrimColor = Color(0xFF121212).copy(0.7f),
                            drawerState = drawerState,
                            gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
                        ) {
                            FeedNavHost(
                                navController = navControllers[MainTab.Feed]!!,
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )
                        }
                    }

                    is MainTab.Inbox -> {
                        DefaultTabContainer(
                            navController = navControllers[MainTab.Inbox]!!,
                            enablePadding = false,
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
                            content = { ProfileNavHost(navController = it, authViewModel = authViewModel) }
                        )
                    }
                }
            }
        }
    }
}
