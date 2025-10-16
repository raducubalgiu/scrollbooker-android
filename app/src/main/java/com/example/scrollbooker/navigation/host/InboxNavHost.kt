package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.inbox.InboxScreen
import com.example.scrollbooker.ui.inbox.InboxViewModel
import com.example.scrollbooker.ui.inbox.EmploymentRespondConsentScreen
import com.example.scrollbooker.ui.inbox.EmploymentRespondScreen
import kotlinx.coroutines.launch

@Composable
fun InboxNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.InboxNavigator.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        navigation(
            route = MainRoute.InboxNavigator.route,
            startDestination = MainRoute.Inbox.route,
        ) {
            composable(MainRoute.Inbox.route) { backStackEntry ->
                val mainNavController = LocalMainNavController.current
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.InboxNavigator.route)
                }

                val viewModel = hiltViewModel<InboxViewModel>(parentEntry)

                val inboxNavigate = remember(mainNavController, navController) {
                    InboxNavigator(
                        mainNavController = mainNavController,
                        navController = navController
                    )
                }

                InboxScreen(
                    viewModel = viewModel,
                    inboxNavigate = inboxNavigate,
                )
            }

            composable(route = "${MainRoute.EmploymentRespond.route}/{employmentId}",
                arguments = listOf(navArgument("employmentId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val employmentId = backStackEntry.arguments?.getInt("employmentId")

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.InboxNavigator.route)
                }
                val viewModel = hiltViewModel<InboxViewModel>(parentEntry)

                EmploymentRespondScreen(
                    employmentId = employmentId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToConsent = { navController.navigate(MainRoute.EmploymentRespondConsent.route) },
                    onDenyEmployment = {
                        navController.currentBackStackEntry?.lifecycleScope?.launch {
                            val denied = viewModel.respondToRequest(status = it)

                            denied.onSuccess {
                                navController.navigate(MainRoute.Inbox.route) {
                                    popUpTo(MainRoute.AppointmentsNavigator.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                )
            }

            composable(route = MainRoute.EmploymentRespondConsent.route,) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.InboxNavigator.route)
                }
                val viewModel = hiltViewModel<InboxViewModel>(parentEntry)

                EmploymentRespondConsentScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAcceptEmployment = {
                        navController.currentBackStackEntry?.lifecycleScope?.launch {
                            val accepted = viewModel.respondToRequest(status = it)

                            accepted.onSuccess {
                                navController.navigate(MainRoute.Inbox.route) {
                                    popUpTo(MainRoute.AppointmentsNavigator.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}