package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.LocalBottomBarController
import com.example.scrollbooker.ui.booking.confirmation.BookingConfirmationScreen
import com.example.scrollbooker.ui.booking.dateTime.BookingDateTimeScreen
import com.example.scrollbooker.ui.booking.services.BookingServicesScreen
import com.example.scrollbooker.ui.booking.specialists.BookingSpecialistsScreen
import com.example.scrollbooker.ui.booking.BookingViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.bookingGraph(
    navController: NavHostController,
    bookingNavigate: BookingNavigator,
) {
    navigation(
        route = MainRoute.BookingNavigator.route,
        startDestination = MainRoute.BookingServices.route,
        arguments = listOf(
            navArgument("businessId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("userId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("businessOwnerId") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("source") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("selectedProductId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("postId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
    ) {
        composable(
            route = MainRoute.BookingServices.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }

            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingServicesScreen(
                viewModel = viewModel,
                bookingNavigate = bookingNavigate
            )
        }

        composable(
            route = MainRoute.BookingSpecialists.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingSpecialistsScreen(
                viewModel = viewModel,
                bookingNavigate = bookingNavigate
            )
        }

        composable(
            route = MainRoute.BookingDateTime.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            BookingDateTimeScreen(
                viewModel = viewModel,
                bookingNavigate = bookingNavigate
            )
        }

        composable(
            route = MainRoute.BookingConfirmation.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.BookingNavigator.route)
            }
            val viewModel = hiltViewModel<BookingViewModel>(parentEntry)

            val bottomBarController = LocalBottomBarController.current

            BookingConfirmationScreen(
                viewModel = viewModel,
                bookingNavigate = bookingNavigate,
                onCreateAppointment = {
                    backStackEntry.lifecycleScope.launch {
                        val result = viewModel.createAppointment()

                        result.onSuccess {
                            bottomBarController.setTab(MainTab.Appointments)
                            bottomBarController.incAppointments()
                            bottomBarController.triggerAppointmentsRefresh(true)

                            navController.popBackStack(
                                route = MainRoute.Feed.route,
                                inclusive = false
                            )
                        }
                    }
                }
            )
        }

    }
}