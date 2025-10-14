package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation(
        route = RootRoute.MAIN,
        startDestination = MainRoute.Tabs.route
    ) {
        composable(route = MainRoute.Tabs.route) {
            val tabsViewModel: TabsViewModel = hiltViewModel()
            val tabsController = remember(tabsViewModel) {
                TabsController(
                    currentTab = tabsViewModel.currentTab,
                    setTab = tabsViewModel::setTab
                )
            }

            val mainUiViewModel: MainUIViewModel = hiltViewModel()

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

            CompositionLocalProvider(
                LocalTabsController provides tabsController,
                LocalBottomBarController provides bottomBarController
            ) {
                MainApplication(onLogout)
            }
        }

        // Global Routes
        globalGraph(navController)
        calendarGraph(navController)
        cameraGraph(navController)
    }
}