package com.example.scrollbooker.ui.onboarding.client

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.scrollbooker.components.core.layout.FormLayout
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.FeatureState

@SuppressLint("MissingPermission")
@Composable
fun CollectClientLocationPermissionScreen(
    viewModel: CollectClientLocationPermissionViewModel,
    onNext: () -> Unit
) {
    val isSaving by viewModel.isSaving.collectAsState()

    FormLayout(
        isEnabled = true,
        isLoading = isSaving is FeatureState.Loading,
        headLine = stringResource(R.string.locationPermission),
        subHeadLine = stringResource(R.string.locationPermissionDescription),
        buttonTitle = stringResource(R.string.save),
        onBack = {},
        onNext = onNext
    ) {

    }
}