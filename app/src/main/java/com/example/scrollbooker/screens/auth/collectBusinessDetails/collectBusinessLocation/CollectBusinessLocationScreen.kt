package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessLocation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.screens.auth.collectBusinessDetails.CollectBusinessDetails

@Composable
fun CollectBusinessLocationScreen(
    viewModel: CollectBusinessLocationViewModel,
    businessTypeName: String?,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    CollectBusinessDetails(
        headLine = stringResource(id = R.string.location),
        subHeadLine = stringResource(id = R.string.addYourBusinessLocation),
        onBack = onBack,
        onNext = onNext,
    ) {
        Text(text = businessTypeName ?: "")
    }
}