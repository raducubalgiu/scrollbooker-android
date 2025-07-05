package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.inbox.EmploymentRequestAcceptScreen
import com.example.scrollbooker.screens.inbox.InboxScreen
import com.example.scrollbooker.screens.inbox.InboxViewModel

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
                onNavigate = { navController.navigate(MainRoute.EmploymentRequestAccept.route) }
            )
        }

        composable(route = MainRoute.EmploymentRequestAccept.route,
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
            //val viewModel = hiltViewModel<InboxViewModel>(backStackEntry)
            EmploymentRequestAcceptScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}