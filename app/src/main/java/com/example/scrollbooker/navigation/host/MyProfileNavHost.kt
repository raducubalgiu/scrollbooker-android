package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.graphs.editProfileGraph
import com.example.scrollbooker.navigation.graphs.myBusinessGraph
import com.example.scrollbooker.navigation.graphs.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.profile.MyProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.MyProfileScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.extensions.isInRoute
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

@Composable
fun MyProfileNavHost(
    viewModel: MyProfileViewModel,
    layoutViewModel: ProfileLayoutViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val posts = layoutViewModel.detailPostsFlow.collectAsLazyPagingItems()

    NavHost(
        navController = navController,
        startDestination = MainRoute.MyProfileNavigator.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
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
            composable(MainRoute.MyProfile.route) {
                val mainNavController = LocalMainNavController.current
                val permissionController = LocalUserPermissions.current

                val profileNavigate = remember(mainNavController, navController) {
                    ProfileNavigator(
                        rootNavController = mainNavController,
                        navController = navController
                    )
                }

                MyProfileScreen(
                    viewModel = viewModel,
                    layoutViewModel = layoutViewModel,
                    permissionController = permissionController,
                    myProfileData = myProfileData,
                    myPosts = myPosts,
                    profileNavigate = profileNavigate,
                    onNavigateToSocial = { socialParams ->
                        val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParams

                        navController.navigate(
                            "${MainRoute.Social.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
                        )
                    }
                )
            }

            composable(
                route = MainRoute.MyProfilePostDetail.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                MyProfilePostDetailScreen(
                    layoutViewModel = layoutViewModel,
                    posts = posts,
                    onBack = { navController.popBackStack() }
                )
            }

            editProfileGraph(navController, viewModel)
            myBusinessGraph(navController)
            settingsGraph(
                navController = navController,
                onLogout = onLogout
            )
        }

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

            val mainNavController = LocalMainNavController.current

            SocialScreen(
                viewModal = viewModel,
                socialParam = socialParams,
                onBack = { navController.popBackStack() },
                onNavigateUserProfile = { mainNavController.navigate("${MainRoute.UserProfile.route}/$it") }
            )
        }
    }
}