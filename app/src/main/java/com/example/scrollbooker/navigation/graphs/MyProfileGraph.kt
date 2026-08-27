package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
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
    viewModel: MyProfileViewModel,
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
            val permissionController = LocalUserPermissions.current

            MyProfileScreen(
                viewModel = viewModel,
                permissionController = permissionController,
                profileNavigate = profileNavigate,
            )
        }

        composable(
            route = MainRoute.MyProfilePostDetail.route,
            arguments = listOf(
                navArgument("postTab") { type = NavType.StringType },
                navArgument("postIndex") { type = NavType.IntType }
            ),
        ) { backStackEntry ->
            val postTabKey = backStackEntry.arguments?.getString("postTab") ?: return@composable
            val postIndex = backStackEntry.arguments?.getInt("postIndex") ?: return@composable

            MyProfilePostDetailScreen(
                postTabKey = postTabKey,
                postIndex = postIndex,
                viewModel = viewModel,
                profileNavigate = profileNavigate,
            )
        }
    }
}