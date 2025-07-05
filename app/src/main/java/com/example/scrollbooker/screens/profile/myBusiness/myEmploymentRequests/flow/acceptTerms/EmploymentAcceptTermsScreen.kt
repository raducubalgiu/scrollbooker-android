package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.EmploymentRequestViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsViewModel
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmploymentAcceptTermsScreen(
    employmentRequestsViewModel: EmploymentRequestsViewModel,
    globalViewModel: EmploymentRequestViewModel,
    localViewModel: EmploymentAcceptTermsViewModel,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    val createState by employmentRequestsViewModel.createRequestState.collectAsState()
    val consentState by localViewModel.consentState.collectAsState()
    val consentId by globalViewModel.consentId.collectAsState()
    val scrollState = rememberScrollState()
    var agreed by remember { mutableStateOf(false) }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.termsAndConditions),
        onBack = onBack
    ) {
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .clip(shape = ShapeDefaults.Small)
                .fillMaxWidth()
                .weight(1f)
                .background(SurfaceBG)
                .padding(BasePadding)
                .verticalScroll(scrollState)
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
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = {
                            agreed = it

                            val consent = (consentState as FeatureState.Success).data

                            if(it) {
                                globalViewModel.assignConsent(consent.id)
                            } else {
                                globalViewModel.assignConsent(null)
                            }
                        }
                    )
                    Text(
                        text = stringResource(R.string.acceptTermsAndConditions),
                        maxLines = Int.MAX_VALUE,
                        style = bodyLarge
                    )
                }
                MainButton(
                    isLoading = createState is FeatureState.Loading,
                    enabled = consentId != null && agreed && createState != FeatureState.Loading,
                    title = stringResource(R.string.sendTheRequest),
                    onClick = onSubmit,
                )
            }
        }
    }
}