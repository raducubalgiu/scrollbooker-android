package com.example.scrollbooker.feature.auth.presentation.collectBusinessDetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.AuthRoute

@Composable
fun CollectBusinessServicesScreen(navController: NavController) {
    CollectBusinessDetails(
        headLine = stringResource(id = R.string.services),
        subHeadLine = stringResource(id = R.string.addYourBusinessServices),
        onBack = { navController.popBackStack() },
        onNext = { navController.navigate(AuthRoute.BusinessSchedules.route) },
        content = {},
    )
}