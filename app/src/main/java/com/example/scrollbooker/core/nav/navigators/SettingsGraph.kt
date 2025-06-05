package com.example.scrollbooker.core.nav.navigators
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
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
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackKey ->
            val viewModel = hiltViewModel<SettingsViewModel>(backStackKey)

            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            MainRoute.Account.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            AccountScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.Privacy.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            PrivacyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.Security.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            SecurityScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.NotificationSettings.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            NotificationSettings(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.Display.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            DisplayScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.ReportProblem.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            ReportProblemScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.Support.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            SupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.TermsAndConditions.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}