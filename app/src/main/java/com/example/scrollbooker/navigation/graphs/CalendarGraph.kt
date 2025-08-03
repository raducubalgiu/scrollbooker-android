package com.example.scrollbooker.navigation.graphs

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.calendar.AppointmentConfirmationScreen
import com.example.scrollbooker.ui.calendar.CalendarScreen
import com.example.scrollbooker.ui.sharedModules.calendar.CalendarViewModel

fun NavGraphBuilder.calendarGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.CalendarNavigator.route,
        startDestination = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}",
    ) {
        composable(
            route = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("slotDuration") { type = NavType.IntType },
                navArgument("productId") { type = NavType.IntType },
                navArgument("productName") { type = NavType.StringType }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
            }

            val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            val slotDuration = backStackEntry.arguments?.getInt("slotDuration") ?: return@composable
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            val productName = backStackEntry.arguments?.getString("productName") ?: return@composable

            CalendarScreen(
                viewModel = viewModel,
                userId = userId,
                slotDuration = slotDuration,
                productId = productId,
                productName = productName,
                onBack = { navController.popBackStack() },
                onNavigateToConfirmation = {
                    navController.navigate(MainRoute.AppointmentConfirmation.route)
                }
            )
        }

        composable(
            route = MainRoute.AppointmentConfirmation.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
            }

            val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

            AppointmentConfirmationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}