package com.example.scrollbooker.feature.auth.presentation.components.collectBusinessDetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.AuthRoute

@Composable
fun CollectBusinessLocationScreen(navController: NavController) {
    CollectBusinessDetails(
        isFirstScreen = true,
        headLine = stringResource(id = R.string.location),
        subHeadLine = stringResource(id = R.string.addYourBusinessLocation),
        onBack = { navController.popBackStack() },
        onNext = { navController.navigate(AuthRoute.BusinessServices.route) },
    ) {
        Text(text = "Business Location Screen")
    }
}