package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.navigators.ReviewsNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.reviews.ReviewsDetailScreen
import com.example.scrollbooker.ui.reviews.ReviewsScreen
import com.example.scrollbooker.ui.reviews.ReviewsViewModel

fun NavGraphBuilder.reviewsGraph(
    navController: NavHostController,
    reviewsNavigate: ReviewsNavigator
) {
    navigation(
        route = MainRoute.ReviewsNavigator.route,
        arguments = listOf(
            navArgument("businessId") { type = NavType.IntType },
            navArgument("employeeId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        startDestination = MainRoute.Reviews.route
    ) {
        composable(
            route = MainRoute.Reviews.route,
            exitTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.ReviewsDetail.route) == true) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.ReviewsDetail.route) == true) {
                    EnterTransition.None
                } else {
                    null
                }
            },
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.ReviewsNavigator.route)
            }
            val viewModel = hiltViewModel<ReviewsViewModel>(parentEntry)

            ReviewsScreen(
                viewModel = viewModel,
                reviewsNavigate = reviewsNavigate
            )
        }

        composable(
            route = MainRoute.ReviewsDetail.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            arguments = listOf(
                navArgument("reviewTab") { type = NavType.StringType },
                navArgument("reviewIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val reviewTabKey = backStackEntry.arguments?.getString("reviewTab") ?: return@composable
            val reviewIndex = backStackEntry.arguments?.getInt("reviewIndex") ?: return@composable

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.ReviewsNavigator.route)
            }
            val viewModel = hiltViewModel<ReviewsViewModel>(parentEntry)

            ReviewsDetailScreen(
                reviewTabKey = reviewTabKey,
                reviewIndex = reviewIndex,
                viewModel = viewModel,
                onBack = { reviewsNavigate.back() },
                onNavigateToUserProfile = { reviewsNavigate.toUserProfile(it) },
                onNavigateToBooking = { product, source -> reviewsNavigate.toBookingFromProduct(product, source) },
                onNavigateToEditPost = { reviewsNavigate.toEditPost(it) }
            )
        }
    }
}
