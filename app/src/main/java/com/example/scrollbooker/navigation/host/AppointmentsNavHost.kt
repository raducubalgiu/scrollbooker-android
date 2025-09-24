package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.MainUIViewModel

@Composable
fun AppointmentsNavHost(
    navController: NavHostController,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.AppointmentsNavigator.route,
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        appointmentsGraph(
            navController = navController,
            appointmentsNumber = appointmentsNumber,
            onChangeTab = onChangeTab,
        )
    }
}