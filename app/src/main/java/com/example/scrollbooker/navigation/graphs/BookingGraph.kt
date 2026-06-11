package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.booking.BookingConfirmationScreen
import com.example.scrollbooker.ui.booking.BookingDateTimeScreen
import com.example.scrollbooker.ui.booking.BookingServicesScreen
import com.example.scrollbooker.ui.booking.BookingSpecialistsScreen
import com.example.scrollbooker.ui.booking.BookingViewModel

fun NavGraphBuilder.bookingGraph(
    navController: NavHostController
) {
    val pushSpec: FiniteAnimationSpec<IntOffset> = tween(320, easing = LinearOutSlowInEasing)
    val popSpec: FiniteAnimationSpec<IntOffset> = tween(280, easing = LinearOutSlowInEasing)
    val fadeInSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)
    val fadeOutSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)

    navigation(
        route = MainRoute.BookingNavigator.route,
        startDestination = MainRoute.BookingServices.route,
        arguments = listOf(
            navArgument("businessId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("userId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("businessOwnerId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("source") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("selectedProductId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
    ) {
        composable(
            route = MainRoute.BookingServices.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }

            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingServicesScreen(
                viewModel = viewModel,
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
                viewModel = viewModel,
                onNavigateToDateTime = { navController.navigate(MainRoute.BookingDateTime.route) },
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
                viewModel = viewModel,
                onNavigateToConfirmation = { navController.navigate(MainRoute.BookingConfirmation.route) },
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
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}