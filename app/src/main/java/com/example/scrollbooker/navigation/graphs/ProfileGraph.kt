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
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.extensions.isInRoute
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.profile.MyProfileScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfileScreen
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>
) {
    navigation(
        route = MainRoute.MyProfileNavigator.route,
        startDestination = MainRoute.MyProfile.route,
        exitTransition = {
            if (targetState.isInRoute(MainRoute.MyProfilePostDetail.route)) {
                ExitTransition.None
            } else {
                slideOutToLeft()
            }
        },
        popEnterTransition = {
            if (initialState.isInRoute(MainRoute.MyProfilePostDetail.route)) {
                EnterTransition.None
            } else {
                slideInFromLeft()
            }
        },
        enterTransition = { slideInFromRight() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(MainRoute.MyProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyProfileViewModel>(backStackEntry)
            val permissionController = LocalUserPermissions.current

            val profileNavigate = remember(navController) {
                ProfileNavigator(navController)
            }

            MyProfileScreen(
                viewModel = viewModel,
                permissionController = permissionController,
                myProfileData = myProfileData,
                myPosts = myPosts,
                profileNavigate = profileNavigate,
            )
        }

        composable("${MainRoute.UserProfile.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

            val profileNavigate = remember(navController) {
                ProfileNavigator(navController)
            }

            UserProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate,
            )
        }

//        composable(
//            route = MainRoute.MyProfilePostDetail.route,
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None },
//            popEnterTransition = { EnterTransition.None },
//            popExitTransition = { ExitTransition.None }
//        ) {
//            MyProfilePostDetailScreen(
//                layoutViewModel = layoutViewModel,
//                posts = posts,
//                onBack = { navController.popBackStack() },
//                onNavigateToUserProfile = {
//                    navController.navigate("${MainRoute.UserProfile.route}/${it}")
//                }
//            )
//        }

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
                onNavigateUserProfile = {
                    navController.navigate("${MainRoute.UserProfile.route}/$it") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}