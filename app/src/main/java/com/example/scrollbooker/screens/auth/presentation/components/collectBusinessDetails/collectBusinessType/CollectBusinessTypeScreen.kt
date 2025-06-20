package com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessType

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessDetails

@Composable
fun CollectBusinessTypeScreen(
    viewModel: CollectBusinessTypeViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    CollectBusinessDetails(
        isFirstScreen = false,
        headLine = stringResource(id = R.string.collectBusinessTypeHeadline),
        subHeadLine = stringResource(id = R.string.collectBusinessTypeSubHeadline),
        onBack = onBack,
        onNext = onNext,
    ) {
        Text(text = "Business Location Screen")
    }
}