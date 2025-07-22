package com.example.scrollbooker.navigation.host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.search.SearchScreen
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen

@Composable
fun SearchNavHost(
    businessTypesState: FeatureState<List<BusinessType>>,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Search.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {
        composable(MainRoute.Search.route) { backStackEntry ->
            val viewModel = hiltViewModel<SearchViewModel>(backStackEntry)
            SearchScreen(
                viewModel = viewModel,
                businessTypesState = businessTypesState,
                onNavigateToBusinessProfile = { navController.navigate(MainRoute.BusinessProfile.route) }
            )
        }
        composable(MainRoute.BusinessProfile.route) { backStackEntry ->
            BusinessProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}