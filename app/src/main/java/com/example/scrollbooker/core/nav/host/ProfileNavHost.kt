package com.example.scrollbooker.core.nav.host
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.navigators.myBusinessGraph
import com.example.scrollbooker.core.nav.navigators.profileGraph
import com.example.scrollbooker.core.nav.navigators.settingsGraph
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.profile.presentation.MyProfileScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.MyProfile.route
    ) {
        composable(MainRoute.MyProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
            MyProfileScreen(
                viewModel = viewModel,
                onNavigate = { navController.navigate(it) }
            )
        }
        profileGraph(navController)
        myBusinessGraph(navController)
        settingsGraph(navController)
    }
}