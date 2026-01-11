package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.unit.IntOffset
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight

@Composable
fun MyProfileNavHost(
    viewModel: MyProfileViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val pushSpec: FiniteAnimationSpec<IntOffset> = tween(320, easing = LinearOutSlowInEasing)
    val popSpec: FiniteAnimationSpec<IntOffset> = tween(280, easing = LinearOutSlowInEasing)
    val fadeInSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)
    val fadeOutSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)

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
                    permissionController = permissionController,
                    myProfileData = myProfileData,
                    myPosts = myPosts,
                    profileNavigate = profileNavigate
                )
            }

            composable(
                route = MainRoute.MyProfilePostDetail.route,
                enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
                exitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutVertically(popSpec) { it } }
            ) {
                val posts = viewModel.detailPostsFlow.collectAsLazyPagingItems()

                MyProfilePostDetailScreen(
                    viewModel = viewModel,
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
    }
}