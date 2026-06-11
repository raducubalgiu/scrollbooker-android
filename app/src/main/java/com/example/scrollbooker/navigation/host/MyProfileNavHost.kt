package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.graphs.cameraGraph
import com.example.scrollbooker.navigation.graphs.editProfileGraph
import com.example.scrollbooker.navigation.graphs.myBusinessGraph
import com.example.scrollbooker.navigation.graphs.myProfileGraph
import com.example.scrollbooker.navigation.graphs.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.remember
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.theme.Background

@Composable
fun MyProfileNavHost(
    viewModel: MyProfileViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val profileNavigate = remember(navController) {
        ProfileNavigator(navController)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoute.MyProfileNavigator.route,
            enterTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.Camera.route) == true) {
                    EnterTransition.None
                } else {
                    slideInFromRight()
                }
            },
            exitTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.Camera.route) == true) {
                    ExitTransition.None
                } else {
                    slideOutToLeft()
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.Camera.route) == true) {
                    EnterTransition.None
                } else {
                    slideInFromLeft()
                }
            },
            popExitTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.Camera.route) == true) {
                    ExitTransition.None
                } else {
                    slideOutToRight()
                }
            }
        ) {
            myProfileGraph(navController, myProfileData, myPosts, profileNavigate)
            userProfileGraph(navController, profileNavigate)

            editProfileGraph(navController, viewModel, profileNavigate)
            myBusinessGraph(navController, profileNavigate)
            settingsGraph(
                navController = navController,
                onLogout = onLogout,
                profileNavigate = profileNavigate
            )
            cameraGraph(navController)
            socialGraph(navController, profileNavigate)
        }
    }
}