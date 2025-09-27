package com.example.scrollbooker.navigation.host
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.graphs.calendarGraph
import com.example.scrollbooker.navigation.graphs.editProfileGraph
import com.example.scrollbooker.navigation.graphs.globalGraph
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.camera.CameraScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfileScreen
import com.example.scrollbooker.ui.shared.userProducts.UserProductsScreen
import com.example.scrollbooker.ui.social.UserSocialScreen
import com.example.scrollbooker.ui.social.UserSocialViewModel

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()

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
                    authViewModel = viewModel,
                    myProfileViewModel = myProfileViewModel,
                    rootNavController = navController
                )
            }

            // Global Routes
            globalGraph(navController = navController)

            composable(
                route = MainRoute.Camera.route,
                enterTransition = {
                    slideInVertically(
                        animationSpec = tween(240, easing = LinearOutSlowInEasing),
                        initialOffsetY = { full -> full }
                    ) + fadeIn(animationSpec = tween(150))
                },
                exitTransition = {
                    slideOutVertically(
                        animationSpec = tween(180, easing = FastOutLinearInEasing),
                        targetOffsetY = { full -> full / 8 }
                    ) + fadeOut(animationSpec = tween(150))
                },
                popEnterTransition = {
                    slideInVertically(
                        animationSpec = tween(200, easing = LinearOutSlowInEasing),
                        initialOffsetY = { full -> full / 8 }
                    ) + fadeIn(animationSpec = tween(150))
                },
                popExitTransition = {
                    slideOutVertically(
                        animationSpec = tween(260, easing = FastOutLinearInEasing),
                        targetOffsetY = { full -> full }
                    ) + fadeOut(animationSpec = tween(150))
                }
            ) { backStackEntry ->
                CameraScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            calendarGraph(navController = navController)
            editProfileGraph(
                navController = navController,
                viewModel = myProfileViewModel
            )

            appointmentsGraph(
                navController = navController,
                appointmentsNumber = 0,
                onChangeTab = {}
            )
        }
    }
}