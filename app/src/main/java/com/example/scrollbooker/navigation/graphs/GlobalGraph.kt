package com.example.scrollbooker.navigation.graphs
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
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfileScreen

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

//            UserProfileScreen(
//
//                viewModel = viewModel,
//                onBack = { navController.popBackStack() },
//                profileNavigate = profileNavigate,
//                onNavigateToSocial = { socialParams ->
//                    val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParams
//
//                    navController.navigate(
//                        "${MainRoute.Social.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
//                    )
//                }
//            )
        }


    }
}