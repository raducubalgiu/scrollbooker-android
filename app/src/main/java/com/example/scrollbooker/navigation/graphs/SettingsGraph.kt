package com.example.scrollbooker.navigation.graphs
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.settings.SettingsScreen
import com.example.scrollbooker.ui.settings.account.AccountScreen
import com.example.scrollbooker.ui.settings.display.DisplayScreen
import com.example.scrollbooker.ui.settings.notifications.NotificationSettings
import com.example.scrollbooker.ui.settings.privacy.PrivacyScreen
import com.example.scrollbooker.ui.settings.reportProblem.presentation.ReportAProblemViewModel
import com.example.scrollbooker.ui.settings.reportProblem.presentation.ReportProblemScreen
import com.example.scrollbooker.ui.settings.security.SecurityScreen
import com.example.scrollbooker.ui.settings.support.SupportScreen
import com.example.scrollbooker.ui.settings.terms.TermsAndConditionsScreen

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        route = MainRoute.SettingsNavigator.route,
        startDestination = MainRoute.Settings.route
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