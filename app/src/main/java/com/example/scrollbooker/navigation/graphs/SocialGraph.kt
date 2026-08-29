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
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.navigators.ReviewsDetailParam
import com.example.scrollbooker.navigation.navigators.SocialParam
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.reviews.ReviewsDetailScreen
import com.example.scrollbooker.ui.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

fun NavGraphBuilder.socialGraph(
    navController: NavHostController,
    profileNavigate: ProfileNavigator
) {
    navigation(
        route = MainRoute.SocialNavigator.route,
        arguments = listOf(
            navArgument("tabIndex") { type = NavType.IntType },
            navArgument("userId") { type = NavType.IntType },
            navArgument("username") { type = NavType.StringType },
            navArgument("businessId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("employeeId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
        ),
        startDestination = MainRoute.Social.route
    ) {
        composable(
            route = MainRoute.Social.route,
            exitTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.SocialReviewsDetail.route) == true) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.SocialReviewsDetail.route) == true) {
                    EnterTransition.None
                } else {
                    null
                }
            },
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.SocialNavigator.route)
            }
            val viewModel = hiltViewModel<SocialViewModel>(parentEntry)
            val reviewsViewModel = hiltViewModel<ReviewsViewModel>(parentEntry)

            val tabIndex = parentEntry.arguments?.getInt("tabIndex") ?: return@composable
            val userId = parentEntry.arguments?.getInt("userId") ?: return@composable
            val username = parentEntry.arguments?.getString("username") ?: return@composable
            val isBusinessOrEmployee = parentEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

            val businessId = parentEntry.arguments?.getInt("businessId").takeIf { it != -1 }
            val employeeId = parentEntry.arguments?.getInt("employeeId").takeIf { it != -1 }

            val socialParams = SocialParam(
                tabIndex = tabIndex,
                userId = userId,
                businessId = businessId,
                employeeId = employeeId,
                username = username,
                isBusinessOrEmployee = isBusinessOrEmployee
            )

            SocialScreen(
                viewModal = viewModel,
                reviewsViewModel = reviewsViewModel,
                socialParam = socialParams,
                onBack = { navController.popBackStack() },
                onNavigateUserProfile = { profileNavigate.toUserProfile(it) },
                onNavigateToVideoReviewDetail = { index ->
                    val route = MainRoute.SocialReviewsDetail.createRoute(
                        ReviewsDetailParam(
                            reviewTab = ReviewsViewModel.ReviewsTab.VIDEO.key,
                            reviewIndex = index
                        )
                    )
                    navController.navigate(route) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = MainRoute.SocialReviewsDetail.route,
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
                navController.getBackStackEntry(MainRoute.SocialNavigator.route)
            }
            val reviewsViewModel = hiltViewModel<ReviewsViewModel>(parentEntry)

            ReviewsDetailScreen(
                reviewTabKey = reviewTabKey,
                reviewIndex = reviewIndex,
                viewModel = reviewsViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToUserProfile = { profileNavigate.toUserProfile(it) },
                onNavigateToBooking = { product, _ -> profileNavigate.toBookingFromProduct(product, BookingSourceEnum.VIDEO_REVIEWS) },
                onNavigateToEditPost = { profileNavigate.toEditPost(it) }
            )
        }
    }
}
