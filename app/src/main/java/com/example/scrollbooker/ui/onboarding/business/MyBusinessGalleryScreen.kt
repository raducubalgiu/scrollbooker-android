package com.example.scrollbooker.ui.onboarding.business

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.ui.profile.myProfile.myBusiness.myBusinessLocation.MyBusinessLocationViewModel

@Composable
fun MyBusinessGalleryScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    FormLayout(
        headLine = stringResource(R.string.photoGallery),
        subHeadLine = stringResource(R.string.photoGalleryDescription),
        buttonTitle = stringResource(R.string.nextStep),
        onBack = onBack,
        onNext = onNext
    ) { }
}