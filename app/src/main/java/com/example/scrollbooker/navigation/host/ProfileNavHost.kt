package com.example.scrollbooker.navigation.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.myBusinessGraph
import com.example.scrollbooker.navigation.navigators.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.profile.calendar.AppointmentConfirmationScreen
import com.example.scrollbooker.ui.profile.calendar.CalendarScreen
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTabViewModel
import com.example.scrollbooker.ui.profile.myProfile.MyProfileScreen
import com.example.scrollbooker.ui.profile.myProfile.ProfileSharedViewModel
import com.example.scrollbooker.ui.profile.myProfile.edit.EditBioScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditFullNameScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditGenderScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfessionScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfileScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditUsernameScreen
import com.example.scrollbooker.ui.profile.postDetail.ProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.social.UserSocialScreen
import com.example.scrollbooker.ui.profile.social.UserSocialViewModel
import com.example.scrollbooker.ui.profile.userProfile.ProfileViewModel
import com.example.scrollbooker.ui.profile.userProfile.UserProfileScreen
import com.example.scrollbooker.ui.sharedModules.calendar.CalendarViewModel

@Composable
fun ProfileNavHost(
    myProfileData: FeatureState<UserProfile>,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.MyProfile.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {
        composable(MainRoute.MyProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
            MyProfileScreen(
                myProfileData = myProfileData,
                viewModel = viewModel,
                onNavigate = { navController.navigate(it) },
                onNavigateToCalendar = {
                    navController.navigate(
                        "${MainRoute.Calendar.route}/${it.userId}/${it.duration}/${it.id}/${it.name}"
                    )
                }
            )
        }
        composable("${MainRoute.ProfilePostDetail.route}/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType }
        )) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")
            val viewModel = hiltViewModel<ProfilePostsTabViewModel>(backStackEntry)

            ProfilePostDetailScreen(
                postId = postId,
                posts = viewModel.userPosts.collectAsLazyPagingItems(),
                onBack = { navController.popBackStack() }
            )
        }
        composable("${MainRoute.UserProfile.route}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            ),
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
        composable(MainRoute.EditProfile.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(MainRoute.EditFullName.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditFullNameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainRoute.EditUsername.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditUsernameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EditProfession.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditProfessionScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EditBio.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditBioScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EditGender.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditGenderScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }

        composable("${MainRoute.UserSocial.route}/{initialPage}/{userId}/{username}",
            arguments = listOf(
                navArgument("initialPage") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getInt("initialPage") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable

            val viewModel = hiltViewModel<UserSocialViewModel>(backStackEntry)

            UserSocialScreen(
                onBack = { navController.popBackStack() },
                viewModal = viewModel,
                initialPage = initialPage,
                username = username,
                onNavigateUserProfile = { navController.navigate("${MainRoute.UserProfile.route}/$it") }
            )
        }

        navigation(
            route = MainRoute.CalendarNavigator.route,
            startDestination = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}"
        ) {
            composable(
                route = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("slotDuration") { type = NavType.IntType },
                    navArgument("productId") { type = NavType.IntType },
                    navArgument("productName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
                }

                val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                val slotDuration = backStackEntry.arguments?.getInt("slotDuration") ?: return@composable
                val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
                val productName = backStackEntry.arguments?.getString("productName") ?: return@composable

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                CalendarScreen(
                    userId = userId,
                    slotDuration = slotDuration,
                    productId = productId,
                    productName = productName,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToConfirmation = {
                        navController.navigate(MainRoute.AppointmentConfirmation.route)
                    }
                )
            }

            composable(route = MainRoute.AppointmentConfirmation.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
                }

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                AppointmentConfirmationScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        myBusinessGraph(navController)
        settingsGraph(
            navController = navController,
            authViewModel = authViewModel
        )
    }
}