package com.example.scrollbooker.ui.inbox
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmploymentRespondConsentScreen(
    viewModel: InboxViewModel,
    onBack: () -> Unit,
    onAcceptEmployment: (EmploymentRequestStatusEnum) -> Unit
) {
    val consent by viewModel.consentState.collectAsState()
    val agreedConsent by viewModel.agreedTerms.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val verticalScroll = rememberScrollState()
    val isEnabled = consent is FeatureState.Success && agreedConsent && !isSaving

    FormLayout(
        headLine = stringResource(R.string.acceptTerms),
        subHeadLine = stringResource(R.string.acceptTermsAndConditions),
        onBack = onBack,
        isEnabled = isEnabled,
        isLoading = isSaving,
        buttonTitle = stringResource(R.string.acceptEmployment),
        onNext = {
            onAcceptEmployment(EmploymentRequestStatusEnum.ACCEPTED)
        }
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
                .verticalScroll(verticalScroll),
            ) {
                when(consent) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        val consent = (consent as FeatureState.Success).data

                        Text(consent.text)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding)
                    .clickable(
                        onClick = { viewModel.setAgreed() },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedConsent,
                    onCheckedChange = { viewModel.setAgreed() }
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