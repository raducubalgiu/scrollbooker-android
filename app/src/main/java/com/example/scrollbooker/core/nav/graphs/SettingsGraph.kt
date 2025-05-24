package com.example.scrollbooker.core.nav.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.settings.presentation.SettingsScreen
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
        composable(MainRoute.Account.route) {
            AccountScreen(navController)
        }

        composable(MainRoute.Privacy.route) {
            PrivacyScreen(navController)
        }

        composable(MainRoute.Security.route) {
            SecurityScreen(navController)
        }

        composable(MainRoute.Settings.route) {
            SettingsScreen(navController)
        }

        composable(MainRoute.NotificationSettings.route) {
            NotificationSettings(navController)
        }

        composable(MainRoute.Display.route) {
            DisplayScreen(navController)
        }

        composable(MainRoute.ReportProblem.route) {
            ReportProblemScreen(navController)
        }

        composable(MainRoute.Support.route) {
            SupportScreen(navController)
        }

        composable(MainRoute.TermsAndConditions.route) {
            TermsAndConditionsScreen(navController)
        }
    }
}