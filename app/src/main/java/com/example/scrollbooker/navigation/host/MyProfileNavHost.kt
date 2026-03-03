package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.graphs.editProfileGraph
import com.example.scrollbooker.navigation.graphs.myBusinessGraph
import com.example.scrollbooker.navigation.graphs.myProfileGraph
import com.example.scrollbooker.navigation.graphs.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel
import com.example.scrollbooker.ui.theme.Background

@Composable
fun MyProfileNavHost(
    viewModel: MyProfileViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoute.MyProfileNavigator.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            myProfileGraph(navController, myProfileData, myPosts)
            userProfileGraph(navController)
            editProfileGraph(navController, viewModel)
            myBusinessGraph(navController)
            settingsGraph(
                navController = navController,
                onLogout = onLogout
            )

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
}