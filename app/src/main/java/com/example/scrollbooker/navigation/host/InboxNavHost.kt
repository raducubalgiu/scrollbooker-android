package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.inbox.InboxScreen
import com.example.scrollbooker.ui.inbox.InboxViewModel
import com.example.scrollbooker.ui.inbox.employmentRespond.EmploymentRespondConsentScreen
import com.example.scrollbooker.ui.inbox.employmentRespond.EmploymentRespondScreen
import com.example.scrollbooker.ui.inbox.employmentRespond.EmploymentRespondViewModel

@Composable
fun InboxNavHost(
    navController: NavHostController,
    appointmentsNumber: Int,
    notificationsNumber: Int,
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Inbox.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(MainRoute.Inbox.route) { backStackEntry ->
            val viewModel = hiltViewModel<InboxViewModel>(backStackEntry)
            val rootNavController = LocalRootNavController.current

            val inboxNavigate = remember(rootNavController, navController) {
                InboxNavigator(
                    rootNavController = rootNavController,
                    navController = navController
                )
            }

            InboxScreen(
                viewModel = viewModel,
                appointmentsNumber = appointmentsNumber,
                notificationsNumber = notificationsNumber,
                inboxNavigate = inboxNavigate,
            )
        }

        navigation(
            route = MainRoute.EmploymentRespondNavigator.route,
            startDestination = "${MainRoute.EmploymentRespond.route}/{employmentId}",
        ) {
            composable(route = "${MainRoute.EmploymentRespond.route}/{employmentId}",
                arguments = listOf(navArgument("employmentId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val employmentId = backStackEntry.arguments?.getInt("employmentId")

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(
                        "${MainRoute.EmploymentRespond.route}/${employmentId}"
                    )
                }
                val viewModel = hiltViewModel<EmploymentRespondViewModel>(parentEntry)

                EmploymentRespondScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToConsent = {
                        navController.navigate("${MainRoute.EmploymentRespondConsent.route}/${employmentId}")
                    }
                )
            }

            composable(route = "${MainRoute.EmploymentRespondConsent.route}/{employmentId}",
                arguments = listOf(navArgument("employmentId") { type = NavType.IntType }),
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val employmentId = backStackEntry.arguments?.getInt("employmentId")

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(
                        "${MainRoute.EmploymentRespond.route}/${employmentId}"
                    )
                }
                val viewModel = hiltViewModel<EmploymentRespondViewModel>(parentEntry)

                EmploymentRespondConsentScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onRespond = { viewModel.respondToRequest(status = it) }
                )
            }
        }
    }
}