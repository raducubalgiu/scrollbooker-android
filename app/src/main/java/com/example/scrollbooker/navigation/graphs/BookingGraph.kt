package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.booking.BookingConfirmationScreen
import com.example.scrollbooker.ui.booking.BookingDateTimeScreen
import com.example.scrollbooker.ui.booking.BookingServicesScreen
import com.example.scrollbooker.ui.booking.BookingSpecialistsScreen
import com.example.scrollbooker.ui.booking.BookingViewModel

fun NavGraphBuilder.bookingGraph(
    navController: NavHostController
) {
    navigation(
        route = MainRoute.BookingNavigator.route,
        startDestination = MainRoute.BookingServices.route,
        arguments = listOf(
            navArgument("businessId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("employeeId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("selectedProductId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(
            route = MainRoute.BookingServices.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }

            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingServicesScreen(
                viewModel=viewModel,
                onNavigateToSpecialists = { navController.navigate(MainRoute.BookingSpecialists.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.BookingSpecialists.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingSpecialistsScreen(
                viewModel=viewModel,
                onNavigateToDateTime = {navController.navigate(MainRoute.BookingDateTime.route)},
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.BookingDateTime.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingDateTimeScreen(
                viewModel=viewModel,
                onNavigateToConfirmation = {navController.navigate(MainRoute.BookingConfirmation.route)},
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.BookingConfirmation.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingConfirmationScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}