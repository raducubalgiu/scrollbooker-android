package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.graphs.calendarGraph
import com.example.scrollbooker.navigation.graphs.sharedProfileGraph
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.shared.userProducts.UserProductsScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()

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
                    rootNavController = navController
                )
            }

            composable(
                route = "${MainRoute.UserProducts.route}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable

                UserProductsScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    onNavigateToCalendar = { (userId, slotDuration, productId, productName) ->
                        navController.navigate(
                            "${MainRoute.Calendar.route}/$userId/$slotDuration/$productId/$productName"
                        )
                    }
                )
            }

            sharedProfileGraph(navController)
            calendarGraph(navController)
        }
    }
}