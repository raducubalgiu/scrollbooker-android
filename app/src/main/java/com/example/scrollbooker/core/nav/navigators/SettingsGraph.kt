package com.example.scrollbooker.core.nav.navigators
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideEnterTransition
import com.example.scrollbooker.core.nav.transitions.slideExitTransition
import com.example.scrollbooker.feature.settings.presentation.SettingsScreen
import com.example.scrollbooker.feature.settings.presentation.SettingsViewModel
import com.example.scrollbooker.feature.settings.presentation.account.AccountScreen
import com.example.scrollbooker.feature.settings.presentation.display.DisplayScreen
import com.example.scrollbooker.feature.settings.presentation.notifications.NotificationSettings
import com.example.scrollbooker.feature.settings.presentation.privacy.PrivacyScreen
import com.example.scrollbooker.feature.settings.presentation.reportProblem.ReportProblemScreen
import com.example.scrollbooker.feature.settings.presentation.security.SecurityScreen
import com.example.scrollbooker.feature.settings.presentation.support.SupportScreen
import com.example.scrollbooker.feature.settings.presentation.terms.TermsAndConditionsScreen

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(
        route = MainRoute.SettingsNavigator.route,
        startDestination = MainRoute.Settings.route,
    ) {
        composable(
            MainRoute.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            }
        ) { backStackKey ->
            val viewModel = hiltViewModel<SettingsViewModel>(backStackKey)

            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(MainRoute.Account.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            AccountScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Privacy.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            PrivacyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Security.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            SecurityScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.NotificationSettings.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            NotificationSettings(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Display.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            DisplayScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.ReportProblem.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            ReportProblemScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Support.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            SupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.TermsAndConditions.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) {
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}