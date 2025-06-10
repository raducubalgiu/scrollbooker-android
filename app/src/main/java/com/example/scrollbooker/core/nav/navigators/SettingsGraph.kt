package com.example.scrollbooker.core.nav.navigators
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
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

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.SettingsNavigator.route,
        startDestination = MainRoute.Settings.route,
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
    ) {
        composable(MainRoute.Settings.route) { backStackKey ->
            val viewModel = hiltViewModel<SettingsViewModel>(backStackKey)

            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(MainRoute.Account.route) {
            AccountScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Privacy.route) {
            PrivacyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Security.route) {
            SecurityScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.NotificationSettings.route) {
            NotificationSettings(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Display.route) {
            DisplayScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.ReportProblem.route) {
            ReportProblemScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Support.route) {
            SupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.TermsAndConditions.route) {
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}