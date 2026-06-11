package com.example.scrollbooker.navigation.host
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.graphs.cameraGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppointmentsNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.AppointmentsNavigator.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        appointmentsGraph(navController)
        userProfileGraph(navController)
        cameraGraph(navController)
        socialGraph(navController)
    }
}