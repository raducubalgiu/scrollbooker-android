package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.profile.MyProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.MyProfileScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel

fun NavGraphBuilder.myProfileGraph(
    navController: NavHostController,
    profileNavigate: ProfileNavigator
) {
    navigation(
        route = MainRoute.MyProfileNavigator.route,
        startDestination = MainRoute.MyProfile.route
    ) {
        composable(
            route = MainRoute.MyProfile.route,
            exitTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.MyProfilePostDetail.route) == true) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.MyProfilePostDetail.route) == true) {
                    EnterTransition.None
                } else {
                    null
                }
            },
        ) { backStackEntry ->
            val viewModel = hiltViewModel<MyProfileViewModel>(backStackEntry)
            val permissionController = LocalUserPermissions.current

            MyProfileScreen(
                viewModel = viewModel,
                permissionController = permissionController,
                profileNavigate = profileNavigate,
            )
        }

        composable(
            route = "${MainRoute.MyProfilePostDetail.route}/{postTab}/{postIndex}",
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            arguments = listOf(
                navArgument("postTab") { type = NavType.StringType },
                navArgument("postIndex") { type = NavType.IntType }
            ),
        ) { backStackEntry ->
            val postTabKey = backStackEntry.arguments?.getString("postTab") ?: return@composable
            val postIndex = backStackEntry.arguments?.getInt("postIndex") ?: return@composable

            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfileNavigator.route)
            }
            val viewModel = hiltViewModel<MyProfileViewModel>(parentBackStackEntry)

            MyProfilePostDetailScreen(
                postTabKey = postTabKey,
                postIndex = postIndex,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate,
            )
        }
    }
}