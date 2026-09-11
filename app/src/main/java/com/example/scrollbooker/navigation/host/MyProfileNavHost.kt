package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.navigation.graphs.bookingGraph
import com.example.scrollbooker.navigation.graphs.postUtilityGraph
import com.example.scrollbooker.navigation.graphs.reviewsGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.navigators.ReviewsNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.theme.Background

private fun isStaticRoute(route: String?): Boolean =
    route != null && (route.startsWith(MainRoute.Camera.route) || route.startsWith(MainRoute.MyProfilePostDetail.route))

private fun isPostUtilityRoute(route: String?): Boolean =
    route != null && (route.startsWith(MainRoute.EditPost.route) || route.startsWith(MainRoute.PostStatistics.route))

@Composable
fun MyProfileNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val viewModel: MyProfileViewModel = hiltViewModel()
    val profileNavigate = remember(navController) { ProfileNavigator(navController) }
    val bookingNavigate = remember(navController) { BookingNavigator(navController) }
    val reviewsNavigate = remember(navController) { ReviewsNavigator(navController) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoute.MyProfileNavigator.route,
            enterTransition = {
                if (isStaticRoute(targetState.destination.route)) {
                    EnterTransition.None
                } else {
                    slideInFromRight()
                }
            },
            exitTransition = {
                val route = targetState.destination.route
                if (isStaticRoute(route) || isPostUtilityRoute(route)) {
                    ExitTransition.None
                } else {
                    slideOutToLeft()
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (isStaticRoute(route) || isPostUtilityRoute(route)) {
                    EnterTransition.None
                } else {
                    slideInFromLeft()
                }
            },
            popExitTransition = {
                if (isStaticRoute(initialState.destination.route)) {
                    ExitTransition.None
                } else {
                    slideOutToRight()
                }
            }
        ) {
            myProfileGraph(viewModel, profileNavigate)
            editProfileGraph(navController, viewModel, profileNavigate)
            userProfileGraph(navController, profileNavigate)

            myBusinessGraph(navController, profileNavigate)
            settingsGraph(
                navController = navController,
                onLogout = onLogout,
                profileNavigate = profileNavigate
            )
            cameraGraph(navController, profileNavigate)
            socialGraph(navController, profileNavigate)
            bookingGraph(navController, bookingNavigate)
            postUtilityGraph(navController)
            reviewsGraph(navController, reviewsNavigate)
        }
    }
}