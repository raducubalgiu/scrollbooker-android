package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.graphs.cameraGraph
import com.example.scrollbooker.navigation.graphs.myProfileGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.AppointmentsNavigator
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun AppointmentsNavHost(navController: NavHostController) {
    val viewModel: MyProfileViewModel = hiltViewModel()

    val profileNavigate = remember(navController) {
        ProfileNavigator(navController)
    }

    val appointmentNavigate = remember(navController) {
        AppointmentsNavigator(navController)
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute.Appointments.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        appointmentsGraph(appointmentNavigate)
        myProfileGraph(viewModel, profileNavigate)
        userProfileGraph(navController, profileNavigate)
        cameraGraph(navController, profileNavigate)
        socialGraph(navController, profileNavigate)
    }
}