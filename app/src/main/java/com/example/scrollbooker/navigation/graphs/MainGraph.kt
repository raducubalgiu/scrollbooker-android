package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.LocalTabsController
import com.example.scrollbooker.navigation.TabsController
import com.example.scrollbooker.navigation.TabsViewModel
import com.example.scrollbooker.navigation.host.MainApplication
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.routes.RootRoute
import com.example.scrollbooker.ui.BottomBarController
import com.example.scrollbooker.ui.LocalBottomBarController
import com.example.scrollbooker.ui.MainUIViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.UserPermissionsController

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    navigation(
        route = RootRoute.MAIN,
        startDestination = MainRoute.Shell.route
    ) {
        composable(route = MainRoute.Shell.route) {
            val tabsViewModel: TabsViewModel = hiltViewModel()

            val tabsController = remember(tabsViewModel) {
                TabsController(
                    currentTab = tabsViewModel.currentTab,
                    setTab = tabsViewModel::setTab
                )
            }

            val mainUiViewModel: MainUIViewModel = hiltViewModel()
            val permissions by mainUiViewModel.permissions.collectAsState()

            val bottomBarController = remember(mainUiViewModel) {
                BottomBarController(
                    appointments = mainUiViewModel.appointments,
                    notifications = mainUiViewModel.notifications,
                    incAppointments = mainUiViewModel::incAppointmentsNumber,
                    decAppointments = mainUiViewModel::decAppointmentsNumber,
                    setAppointments = mainUiViewModel::setAppointments,
                    setNotifications = mainUiViewModel::setNotifications,
                )
            }

            val permissionsController = remember(permissions) {
                UserPermissionsController(permissions)
            }

            val mainNavController = rememberNavController()

            CompositionLocalProvider(
                LocalMainNavController provides mainNavController,
                LocalTabsController provides tabsController,
                LocalBottomBarController provides bottomBarController,
                LocalUserPermissions provides permissionsController
            ) {
                NavHost(
                    navController = mainNavController,
                    startDestination = MainRoute.Tabs.route
                ) {
                    composable(MainRoute.Tabs.route) {
                        MainApplication(onLogout)
                    }

                    // Global Routes
                    globalGraph(mainNavController)
                    calendarGraph(mainNavController)
                    cameraGraph(mainNavController)
                }
            }
        }
    }
}