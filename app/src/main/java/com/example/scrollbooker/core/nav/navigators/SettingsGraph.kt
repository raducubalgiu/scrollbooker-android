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
import com.example.scrollbooker.screens.profile.myProfile.settings.SettingsScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.account.AccountScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.display.DisplayScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.notifications.NotificationSettings
import com.example.scrollbooker.screens.profile.myProfile.settings.privacy.PrivacyScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.presentation.ReportAProblemViewModel
import com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.presentation.ReportProblemScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.security.SecurityScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.support.SupportScreen
import com.example.scrollbooker.screens.profile.myProfile.settings.terms.TermsAndConditionsScreen

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
        composable(MainRoute.Settings.route) {
            SettingsScreen(
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