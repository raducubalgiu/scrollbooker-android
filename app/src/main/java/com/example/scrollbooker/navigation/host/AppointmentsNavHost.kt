package com.example.scrollbooker.navigation.host
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.routes.RootRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import androidx.compose.runtime.getValue

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppointmentsNavHost(navController: NavHostController) {
    val root = LocalRootNavController.current
    val mainEntry = remember(root) { root.getBackStackEntry(RootRoute.MAIN) }

    val appointmentCreated by mainEntry.savedStateHandle
        .getStateFlow("APPOINTMENT_CREATED", false)
        .collectAsState()

    NavHost(
        navController = navController,
        startDestination = MainRoute.AppointmentsNavigator.route,
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {

        appointmentsGraph(
            navController = navController,
            appointmentCreated = appointmentCreated
        )
    }
}