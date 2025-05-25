package com.example.scrollbooker.core.nav.navigators
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
        ) {
            SettingsScreen(navController)
        }

        composable(
            MainRoute.Account.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            AccountScreen(navController)
        }

        composable(
            MainRoute.Privacy.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            PrivacyScreen(navController)
        }

        composable(
            MainRoute.Security.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            SecurityScreen(navController)
        }

        composable(
            MainRoute.NotificationSettings.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            NotificationSettings(navController)
        }

        composable(
            MainRoute.Display.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            DisplayScreen(navController)
        }

        composable(
            MainRoute.ReportProblem.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            ReportProblemScreen(navController)
        }

        composable(
            MainRoute.Support.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            SupportScreen(navController)
        }

        composable(
            MainRoute.TermsAndConditions.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            TermsAndConditionsScreen(navController)
        }
    }
}