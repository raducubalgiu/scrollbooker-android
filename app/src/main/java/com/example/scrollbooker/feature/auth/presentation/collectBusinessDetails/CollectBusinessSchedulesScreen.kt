package com.example.scrollbooker.feature.auth.presentation.collectBusinessDetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R

@Composable
fun CollectBusinessSchedulesScreen(navController: NavController) {
    CollectBusinessDetails(
        isLastScreen = true,
        headLine = stringResource(id = R.string.schedule),
        subHeadLine = stringResource(id = R.string.addYourBusinessSchedule),
        onBack = { navController.popBackStack() },
        onNext = {},
        content = {},
    )
}