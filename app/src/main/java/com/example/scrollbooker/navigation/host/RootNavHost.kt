package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.graphs.calendarGraph
import com.example.scrollbooker.navigation.graphs.globalGraph
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.ui.auth.AuthViewModel

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()

    when(val state = authState) {
        is FeatureState.Success -> {
            if(state.data.isValidated) {
                NavHost(
                    navController = navController,
                    startDestination = GlobalRoute.MAIN
                ) {
                    composable(GlobalRoute.MAIN) {
                        MainApplication(
                            authViewModel = viewModel,
                            rootNavController = navController,
                        )
                    }

                    // Global Routes
                    globalGraph(navController = navController)
                    calendarGraph(navController = navController)
                    appointmentsGraph(
                        rootNavController = navController,
                        navController = navController,
                        appointmentsNumber = 0,
                        notificationsNumber = 0,
                    )
                }
            } else {
                AuthNavHost(viewModel)
            }
        }
        is FeatureState.Error -> GlobalRoute.AUTH
        else -> null
    }
}