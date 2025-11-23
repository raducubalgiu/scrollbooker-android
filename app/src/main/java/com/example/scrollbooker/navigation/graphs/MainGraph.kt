package com.example.scrollbooker.navigation.graphs
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.LocationController
import com.example.scrollbooker.ui.LocationViewModel
import com.example.scrollbooker.ui.PrecisionMode
import com.example.scrollbooker.ui.UserPermissionsController

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    navigation(
        route = RootRoute.MAIN,
        startDestination = MainRoute.Shell.route
    ) {
        composable(route = MainRoute.Shell.route) {
            val context = LocalContext.current
            val tabsViewModel: TabsViewModel = hiltViewModel()
            val mainUiViewModel: MainUIViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()

            val permissions by mainUiViewModel.permissions.collectAsState()

            val tabsController = remember(tabsViewModel) {
                TabsController(
                    currentTab = tabsViewModel.currentTab,
                    setTab = tabsViewModel::setTab
                )
            }

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

            val locationController = remember(locationViewModel) {
                LocationController(
                    stateFlow = locationViewModel.state,
                    startUpdates = locationViewModel::startLocationUpdates,
                    stopUpdates = locationViewModel::stopLocationUpdates,
                    onPermissionResult = locationViewModel::onPermissionResult
                )
            }



            val mainNavController = rememberNavController()

            CompositionLocalProvider(
                LocalMainNavController provides mainNavController,
                LocalTabsController provides tabsController,
                LocalBottomBarController provides bottomBarController,
                LocalUserPermissions provides permissionsController,
                LocalLocationController provides locationController
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