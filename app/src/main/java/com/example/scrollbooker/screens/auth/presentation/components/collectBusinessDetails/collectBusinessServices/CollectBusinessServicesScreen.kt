package com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessServices

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessDetails

@Composable
fun CollectBusinessServicesScreen(
    viewModel: CollectBusinessServicesViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    CollectBusinessDetails(
        headLine = stringResource(id = R.string.services),
        subHeadLine = stringResource(id = R.string.addYourBusinessServices),
        onBack = onBack,
        onNext = onNext,
    ) {
        Text(text = "Business Services Screen")
    }
}