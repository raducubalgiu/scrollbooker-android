package com.example.scrollbooker.navigation.graphs

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.camera.CameraScreen
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfileScreen
import com.example.scrollbooker.ui.shared.userProducts.UserProductsScreen
import com.example.scrollbooker.ui.social.UserSocialScreen
import com.example.scrollbooker.ui.social.UserSocialViewModel

fun NavGraphBuilder.globalGraph(
    navController: NavHostController
) {
    navigation(
        route = MainRoute.GlobalRouteNavigator.route,
        startDestination = "${MainRoute.UserProfile.route}/{userId}",
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable("${MainRoute.UserProfile.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

            val profileNavigate = remember(navController) {
                ProfileNavigator(
                    rootNavController = navController,
                    navController = navController
                )
            }

            UserProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate
            )
        }

        composable("${MainRoute.ProfilePostDetail.route}/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")
            //val viewModel = hiltViewModel<ProfilePostsTabViewModel>(backStackEntry)

//                ProfilePostDetailScreen(
//                    postId = postId,
//                    posts = viewModel.userPosts.collectAsLazyPagingItems(),
//                    onBack = { navController.popBackStack() }
//                )
        }

        composable(route = "${MainRoute.UserSocial.route}/{initialPage}/{userId}/{username}/{isBusinessOrEmployee}",
            arguments = listOf(
                navArgument("initialPage") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType },
                navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getInt("initialPage") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            val isBusinessOrEmployee = backStackEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

            val viewModel = hiltViewModel<UserSocialViewModel>(backStackEntry)

            UserSocialScreen(
                viewModal = viewModel,
                initialPage = initialPage,
                username = username,
                isBusinessOrEmployee = isBusinessOrEmployee,
                onBack = { navController.popBackStack() },
                onNavigateUserProfile = { navController.navigate("${MainRoute.UserProfile.route}/$it") }
            )
        }

        composable(
            route = "${MainRoute.UserProducts.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable

            UserProductsScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onNavigateToCalendar = { (userId, slotDuration, productId, productName) ->
                    navController.navigate(
                        "${MainRoute.Calendar.route}/$userId/$slotDuration/$productId/$productName"
                    )
                }
            )
        }

        composable(
            route = MainRoute.Camera.route,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(240, easing = LinearOutSlowInEasing),
                    initialOffsetY = { full -> full }
                ) + fadeIn(animationSpec = tween(150))
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(180, easing = FastOutLinearInEasing),
                    targetOffsetY = { full -> full / 8 }
                ) + fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                slideInVertically(
                    animationSpec = tween(200, easing = LinearOutSlowInEasing),
                    initialOffsetY = { full -> full / 8 }
                ) + fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutVertically(
                    animationSpec = tween(260, easing = FastOutLinearInEasing),
                    targetOffsetY = { full -> full }
                ) + fadeOut(animationSpec = tween(150))
            }
        ) { backStackEntry ->
            CameraScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}