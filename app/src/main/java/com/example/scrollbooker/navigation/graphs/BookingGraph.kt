package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
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
    // Curbe de easing mai fluide (FastOutSlowIn pentru tranziții naturale)
    val pushSpec: FiniteAnimationSpec<IntOffset> = tween(400, easing = FastOutSlowInEasing)
    val popSpec: FiniteAnimationSpec<IntOffset> = tween(350, easing = FastOutSlowInEasing)

    // Animații orizontale standard pentru pașii din interiorul booking-ului
    val stepEnterSpec: FiniteAnimationSpec<IntOffset> = tween(300, easing = FastOutSlowInEasing)
    val stepExitSpec: FiniteAnimationSpec<IntOffset> = tween(300, easing = FastOutSlowInEasing)
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
        enterTransition = { slideInVertically(pushSpec) { it } },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { slideOutVertically(popSpec) { it } }
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