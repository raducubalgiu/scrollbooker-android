package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scrollbooker.core.extensions.isInRoute
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.LocationController
import com.example.scrollbooker.ui.LocationViewModel
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfileScreen
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    navigation(
        route = RootRoute.MAIN,
        startDestination = MainRoute.Shell.route
    ) {
        composable(route = MainRoute.Shell.route) {
            val tabsViewModel: TabsViewModel = hiltViewModel()
            val mainUiViewModel: MainUIViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()

            val permissions by mainUiViewModel.permissions.collectAsStateWithLifecycle()
            val hasEmployees by mainUiViewModel.hasEmployees.collectAsStateWithLifecycle()

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

            val permissionsController = remember(permissions, hasEmployees) {
                UserPermissionsController(permissions, hasEmployees)
            }

            val locationController = remember(locationViewModel) {
                LocationController(
                    stateFlow = locationViewModel.state,
                    startUpdates = locationViewModel::startLocationUpdates,
                    stopUpdates = locationViewModel::stopLocationUpdates,
                    onPermissionResult = locationViewModel::onPermissionResult,
                    syncInitialPermission = locationViewModel::syncInitialPermission
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
                    cameraGraph(mainNavController)

                    navigation(
                        route = MainRoute.UserProfileNavigator.route,
                        startDestination = "${MainRoute.UserProfile.route}/{userId}",
                        exitTransition = {
                            if (targetState.isInRoute(MainRoute.UserProfilePostDetail.route)) {
                                ExitTransition.None
                            } else {
                                slideOutToLeft()
                            }
                        },
                        popEnterTransition = {
                            if (initialState.isInRoute(MainRoute.UserProfilePostDetail.route)) {
                                EnterTransition.None
                            } else {
                                slideInFromLeft()
                            }
                        },
                        enterTransition = { slideInFromRight() },
                        popExitTransition = { slideOutToRight() }
                    ) {
                        composable("${MainRoute.UserProfile.route}/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

                            val profileNavigate = remember(mainNavController) {
                                ProfileNavigator(
                                    rootNavController = mainNavController,
                                    navController = mainNavController
                                )
                            }

                            UserProfileScreen(
                                viewModel = viewModel,
                                onBack = { mainNavController.popBackStack() },
                                profileNavigate = profileNavigate
                            )
                        }
                    }

                    composable(route = "${MainRoute.Social.route}/{tabIndex}/{userId}/{username}/{isBusinessOrEmployee}",
                        arguments = listOf(
                            navArgument("tabIndex") { type = NavType.IntType },
                            navArgument("userId") { type = NavType.IntType },
                            navArgument("username") { type = NavType.StringType },
                            navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: return@composable
                        val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                        val username = backStackEntry.arguments?.getString("username") ?: return@composable
                        val isBusinessOrEmployee = backStackEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

                        val viewModel = hiltViewModel<SocialViewModel>(backStackEntry)
                        val socialParams = NavigateSocialParam(tabIndex, userId, username, isBusinessOrEmployee)

                        SocialScreen(
                            viewModal = viewModel,
                            socialParam = socialParams,
                            onBack = { mainNavController.popBackStack() },
                            onNavigateUserProfile = {
                                mainNavController.navigate("${MainRoute.UserProfile.route}/$it")
                            }
                        )
                    }
                }
            }
        }
    }
}