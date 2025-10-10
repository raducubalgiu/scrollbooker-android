package com.example.scrollbooker.ui.inbox.employmentRespond
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Divider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmploymentRespondConsentScreen(
    viewModel: EmploymentRespondViewModel,
    onBack: () -> Unit,
    onRespond: (EmploymentRequestStatusEnum) -> Unit
) {
    val consent by viewModel.consentState.collectAsState()
    val agreedConsent by viewModel.agreedTerms.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val verticalScroll = rememberScrollState()
    val isEnabled = consent is FeatureState.Success && agreedConsent && isSaving != FeatureState.Loading

    Layout(
        headerTitle = stringResource(R.string.termsAndConditions),
        modifier = Modifier.statusBarsPadding(),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = BasePadding)
                    .verticalScroll(verticalScroll)
            ) {
                Spacer(Modifier.height(BasePadding))
                when(consent) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        val consent = (consent as FeatureState.Success).data

                        Text(consent.text)
                    }
                }
                Spacer(Modifier.height(BasePadding))
            }
            Column {
                HorizontalDivider(color = Divider, thickness = 0.5.dp)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BasePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreedConsent,
                        onCheckedChange = { viewModel.setAgreed(it) }
                    )
                    Text(
                        text = stringResource(R.string.acceptTermsAndConditions),
                        maxLines = Int.MAX_VALUE,
                        style = bodyLarge
                    )
                }
                MainButton(
                    modifier = Modifier
                        .padding(
                            vertical = SpacingM,
                            horizontal = BasePadding
                        ),
                    enabled = isEnabled,
                    isLoading = isSaving is FeatureState.Loading,
                    title = "Accepta angajarea",
                    onClick = {
                        onRespond(EmploymentRequestStatusEnum.ACCEPTED)
                    }
                )
            }
        }
    }
}