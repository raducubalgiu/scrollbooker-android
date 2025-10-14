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

            CompositionLocalProvider(LocalTabsController provides tabsController) {
                MainApplication(onLogout)
            }
        }

        // Global Routes
        globalGraph(navController)
        calendarGraph(navController)
        cameraGraph(navController)
    }
}