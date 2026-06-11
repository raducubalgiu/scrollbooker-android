package com.example.scrollbooker.navigation.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

fun NavGraphBuilder.socialGraph(
    navController: NavHostController
) {
    composable(
        route = "${MainRoute.Social.route}/{tabIndex}/{userId}/{username}/{isBusinessOrEmployee}",
        arguments = listOf(
            navArgument("tabIndex") { type = NavType.IntType },
            navArgument("userId") { type = NavType.IntType },
            navArgument("username") { type = NavType.StringType },
            navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
        )
    ) { backStackEntry ->
        val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: return@composable
        val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
        val username = backStackEntry.arguments?.getString("username") ?: return@composable
        val isBusinessOrEmployee = backStackEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

        val viewModel = hiltViewModel<SocialViewModel>(backStackEntry)
        val socialParams = NavigateSocialParam(tabIndex, userId, username, isBusinessOrEmployee)

        SocialScreen(
            viewModal = viewModel,
            socialParam = socialParams,
            onBack = { navController.popBackStack() },
            onNavigateUserProfile = { userId, username ->
                navController.navigate("${MainRoute.UserProfile.route}/$userId/$username") {
                    launchSingleTop = true
                }
            }
        )
    }
}