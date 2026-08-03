package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.core.extensions.isInRoute
import com.example.scrollbooker.navigation.navigators.AppointmentsNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.appointments.AppointmentDetailsScreen
import com.example.scrollbooker.ui.appointments.AppointmentDetailsViewModel
import com.example.scrollbooker.ui.appointments.AppointmentsScreen
import com.example.scrollbooker.ui.appointments.AppointmentsViewModel

fun NavGraphBuilder.appointmentsGraph(
    appointmentsNavigate: AppointmentsNavigator
) {
    composable(route = MainRoute.Appointments.route) {
        val viewModel = hiltViewModel<AppointmentsViewModel>()

        AppointmentsScreen(
            viewModel = viewModel,
            appointmentsNavigate = appointmentsNavigate
        )
    }

    composable(
        route = "${MainRoute.AppointmentDetails.route}/{appointmentId}",
        arguments = listOf(navArgument("appointmentId") { type = NavType.IntType }),
        exitTransition = {
            if (targetState.isInRoute(MainRoute.Camera.route)) {
                ExitTransition.None
            } else {
                slideOutToLeft()
            }
        },
        popEnterTransition = {
            if (initialState.isInRoute(MainRoute.Camera.route)) {
                EnterTransition.None
            } else {
                slideInFromLeft()
            }
        },
        enterTransition = { slideInFromRight() },
        popExitTransition = { slideOutToRight() },
    ) {
        val viewModel = hiltViewModel<AppointmentDetailsViewModel>()

        AppointmentDetailsScreen(
            viewModel = viewModel,
            appointmentsNavigate = appointmentsNavigate
        )
    }
}
