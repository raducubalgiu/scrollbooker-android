package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.myBusinessGraph
import com.example.scrollbooker.navigation.navigators.settingsGraph
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.main.MainUIViewModel
import com.example.scrollbooker.ui.calendar.AppointmentConfirmationScreen
import com.example.scrollbooker.ui.calendar.CalendarScreen
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
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTabViewModel
import com.example.scrollbooker.ui.profile.userProfile.ProfileViewModel
import com.example.scrollbooker.ui.profile.userProfile.UserProfileScreen
import com.example.scrollbooker.ui.sharedModules.calendar.CalendarViewModel

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val mainViewModel: MainUIViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()
    val myProfileData by mainViewModel.userProfileState.collectAsState()

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
                MainNavHost(
                    rootNavController = navController,
                    mainViewModel = mainViewModel,
                    myProfileData = myProfileData
                )
            }

            composable("${MainRoute.UserProfile.route}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
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
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
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
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
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

            navigation(
                route = MainRoute.CalendarNavigator.route,
                startDestination = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}",
            ) {
                composable(
                    route = "${MainRoute.Calendar.route}/{userId}/{slotDuration}/{productId}/{productName}",
                    arguments = listOf(
                        navArgument("userId") { type = NavType.IntType },
                        navArgument("slotDuration") { type = NavType.IntType },
                        navArgument("productId") { type = NavType.IntType },
                        navArgument("productName") { type = NavType.StringType }
                    ),
                    enterTransition = { slideInFromRight() },
                    exitTransition = { slideOutToLeft() },
                    popEnterTransition = { slideInFromLeft() },
                    popExitTransition = { slideOutToRight() }
                ) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
                    }

                    val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                    val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                    val slotDuration = backStackEntry.arguments?.getInt("slotDuration") ?: return@composable
                    val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
                    val productName = backStackEntry.arguments?.getString("productName") ?: return@composable

                    CalendarScreen(
                        viewModel = viewModel,
                        userId = userId,
                        slotDuration = slotDuration,
                        productId = productId,
                        productName = productName,
                        onBack = { navController.popBackStack() },
                        onNavigateToConfirmation = {
                            navController.navigate(MainRoute.AppointmentConfirmation.route)
                        }
                    )
                }

                composable(
                    route = MainRoute.AppointmentConfirmation.route,
                    enterTransition = { slideInFromRight() },
                    exitTransition = { slideOutToLeft() },
                    popEnterTransition = { slideInFromLeft() },
                    popExitTransition = { slideOutToRight() }
                ) { backStackEntry ->
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
                authViewModel = viewModel
            )
        }
    }
}