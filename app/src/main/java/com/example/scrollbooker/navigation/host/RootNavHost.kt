package com.example.scrollbooker.navigation.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.profile.postDetail.ProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.social.UserSocialScreen
import com.example.scrollbooker.ui.profile.social.UserSocialViewModel
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTabViewModel
import com.example.scrollbooker.ui.profile.userProfile.ProfileViewModel
import com.example.scrollbooker.ui.profile.userProfile.UserProfileScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()

    val startDestination = when(val state = authState) {
        is FeatureState.Success -> {
            if(state.data.isValidated) {
                GlobalRoute.MAIN
            } else {
                GlobalRoute.AUTH
            }
        }
        is FeatureState.Error -> GlobalRoute.AUTH
        else -> null
    }

    if(startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(GlobalRoute.AUTH) {
                AuthNavHost(viewModel)
            }

            composable(GlobalRoute.MAIN) {
                MainNavHost(authViewModel = viewModel)
            }

            composable("${MainRoute.UserProfile.route}/{userId}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType }
                ),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) { backStackEntry ->
                val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)
                UserProfileScreen(
                    viewModel = viewModel,
                    onNavigate = { navController.navigate(it) },
                    onBack = { navController.popBackStack() },
                    onNavigateToCalendar = {
                        navController.navigate(
                            "${MainRoute.Calendar.route}/${it.userId}/${it.duration}/${it.id}/${it.name}"
                        )
                    }
                )
            }

            composable("${MainRoute.ProfilePostDetail.route}/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId")
                val viewModel = hiltViewModel<ProfilePostsTabViewModel>(backStackEntry)

                ProfilePostDetailScreen(
                    postId = postId,
                    posts = viewModel.userPosts.collectAsLazyPagingItems(),
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = "${MainRoute.UserSocial.route}/{initialPage}/{userId}/{username}/{isBusinessOrEmployee}",
                arguments = listOf(
                    navArgument("initialPage") { type = NavType.IntType },
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("username") { type = NavType.StringType },
                    navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
                ),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
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
        }
    }
}