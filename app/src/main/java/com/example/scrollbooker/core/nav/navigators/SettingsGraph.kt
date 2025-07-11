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
import com.example.scrollbooker.screens.auth.AuthViewModel
import com.example.scrollbooker.screens.profile.settings.SettingsScreen
import com.example.scrollbooker.screens.profile.settings.SettingsViewModel
import com.example.scrollbooker.screens.profile.settings.account.AccountScreen
import com.example.scrollbooker.screens.profile.settings.display.DisplayScreen
import com.example.scrollbooker.screens.profile.settings.notifications.NotificationSettings
import com.example.scrollbooker.screens.profile.settings.privacy.PrivacyScreen
import com.example.scrollbooker.screens.profile.settings.reportProblem.presentation.ReportProblemScreen
import com.example.scrollbooker.screens.profile.settings.security.SecurityScreen
import com.example.scrollbooker.screens.profile.settings.support.SupportScreen
import com.example.scrollbooker.screens.profile.settings.terms.TermsAndConditionsScreen
import com.example.scrollbooker.screens.profile.settings.reportProblem.presentation.ReportAProblemViewModel

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
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
                onNavigate = { navController.navigate(it) },
                onLogout = { authViewModel.logout() }
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

        composable(MainRoute.ReportProblem.route) { backStackEntry ->
            val viewModel = hiltViewModel<ReportAProblemViewModel>(backStackEntry)

            ReportProblemScreen(
                viewModel = viewModel,
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