package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.inbox.InboxScreen
import com.example.scrollbooker.screens.inbox.InboxViewModel
import com.example.scrollbooker.screens.inbox.employmentRequestRespond.EmploymentRequestRespondConsentScreen
import com.example.scrollbooker.screens.inbox.employmentRequestRespond.EmploymentRequestRespondScreen
import com.example.scrollbooker.screens.inbox.employmentRequestRespond.EmploymentRequestRespondViewModel

@Composable
fun InboxNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Inbox.route
    ) {
        composable(MainRoute.Inbox.route) { backStackEntry ->
            val viewModel = hiltViewModel<InboxViewModel>(backStackEntry)

            InboxScreen(
                viewModel = viewModel,
                onNavigate = { employmentId ->
                    navController.navigate("${MainRoute.EmploymentRequestRespond.route}/${employmentId}")
                }
            )
        }

        navigation(
            route = MainRoute.EmploymentRequestRespondNavigator.route,
            startDestination = "${MainRoute.EmploymentRequestRespond.route}/{employmentId}",
        ) {
            composable(route = "${MainRoute.EmploymentRequestRespond.route}/{employmentId}",
                arguments = listOf(navArgument("employmentId") { type = NavType.IntType }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) { backStackEntry ->
                val employmentId = backStackEntry.arguments?.getInt("employmentId") ?: -1

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(
                        "${MainRoute.EmploymentRequestRespond.route}/${employmentId}"
                    )
                }
                val viewModel = hiltViewModel<EmploymentRequestRespondViewModel>(parentEntry)

                EmploymentRequestRespondScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigate = {
                        navController.navigate("${MainRoute.EmploymentRequestRespondConsent.route}/${employmentId}")
                    }
                )
            }

            composable(route = "${MainRoute.EmploymentRequestRespondConsent.route}/{employmentId}",
                arguments = listOf(navArgument("employmentId") { type = NavType.IntType }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) { backStackEntry ->
                val employmentId = backStackEntry.arguments?.getInt("employmentId") ?: -1

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(
                        "${MainRoute.EmploymentRequestRespond.route}/${employmentId}"
                    )
                }
                val viewModel = hiltViewModel<EmploymentRequestRespondViewModel>(parentEntry)

                EmploymentRequestRespondConsentScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onRespond = { viewModel.respondToRequest(status = it) }
                )
            }
        }
    }
}