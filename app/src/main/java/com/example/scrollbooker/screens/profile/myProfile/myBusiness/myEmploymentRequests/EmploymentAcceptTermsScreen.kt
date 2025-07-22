package com.example.scrollbooker.screens.profile.myProfile.myBusiness.myEmploymentRequests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.SurfaceBG
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmploymentAcceptTermsScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val consentState by viewModel.consentState.collectAsState()
    val agreedConsent by viewModel.agreedConsent.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val scrollState = rememberScrollState()

    FormLayout(
        modifier = Modifier.safeDrawingPadding(),
        headLine = stringResource(R.string.acceptTerms),
        subHeadLine = stringResource(R.string.acceptTermsForFinishingEmployeeOnboarding),
        onBack = onBack,
        isEnabled = agreedConsent && !isSaving,
        isLoading = isSaving,
        buttonTitle = stringResource(R.string.sendAnEmploymentRequest),
        onNext = onNext
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpacingXL),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .clip(shape = ShapeDefaults.Medium)
                .background(SurfaceBG)
                .padding(SpacingS)
                .verticalScroll(scrollState),
            ) {
                when(consentState) {
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success -> {
                        val consent = (consentState as FeatureState.Success).data

                        Text(consent.text)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedConsent,
                    onCheckedChange = {
                       viewModel.setAgreedConsent(it)
                    }
                )
                Text(
                    text = stringResource(R.string.acceptTermsAndConditions),
                    maxLines = Int.MAX_VALUE,
                    style = bodyLarge
                )
            }
        }
    }
}