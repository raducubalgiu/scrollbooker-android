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
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.graphs.editProfileGraph
import com.example.scrollbooker.navigation.graphs.myBusinessGraph
import com.example.scrollbooker.navigation.graphs.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.MyProfileScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun MyProfileNavHost(
    viewModel: MyProfileViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    navController: NavHostController,
    appointmentsNumber: Int,
    notificationsNumber: Int,
    onLogout: () -> Unit
) {
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
            composable(
                route = MainRoute.MyProfile.route,
                popEnterTransition = { EnterTransition.None},
                popExitTransition = { ExitTransition.None }
            ) {
                val rootNavController = LocalRootNavController.current

                val profileNavigate = remember(rootNavController, navController) {
                    ProfileNavigator(
                        rootNavController = rootNavController,
                        navController = navController
                    )
                }

                MyProfileScreen(
                    viewModel = viewModel,
                    myProfileData = myProfileData,
                    myPosts = myPosts,
                    appointmentsNumber = appointmentsNumber,
                    notificationsNumber = notificationsNumber,
                    profileNavigate = profileNavigate
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