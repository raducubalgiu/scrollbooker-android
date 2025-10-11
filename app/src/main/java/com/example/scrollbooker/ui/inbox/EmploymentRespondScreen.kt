package com.example.scrollbooker.ui.inbox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.dialog.DialogConfirm
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.inbox.components.EmploymentDetails
import com.example.scrollbooker.ui.inbox.components.EmploymentRespondBottomBar
import com.example.scrollbooker.ui.inbox.components.EmploymentRespondParagraphs

@Composable
fun EmploymentRespondScreen(
    employmentId: Int?,
    viewModel: InboxViewModel,
    onBack: () -> Unit,
    onNavigateToConsent: () -> Unit,
    onDenyEmployment: (EmploymentRequestStatusEnum) -> Unit
) {
    var openModal by remember { mutableStateOf(false) }
    val isSaving by viewModel.isSaving.collectAsState()
    val employmentRequest by viewModel.employmentRequestState.collectAsState()
    val verticalScroll = rememberScrollState()

    LaunchedEffect(employmentId) {
        if(employmentId != null) {
            viewModel.setEmploymentId(employmentId)
            viewModel.loadEmploymentRequest()
            viewModel.loadConsent()
        }
    }

    if(openModal) {
        DialogConfirm(
            onDismissRequest = { openModal = false },
            onConfirmation = {
                onDenyEmployment(EmploymentRequestStatusEnum.DENIED)
                openModal = true
            },
            title = stringResource(R.string.denyEmploymentRequest),
            text = stringResource(R.string.areYouSureDenyEmployment),
            confirmText = stringResource(R.string.deny)
        )
    }

    Layout(
        modifier = Modifier.background(Background),
        enablePaddingH = false,
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when(val employment = employmentRequest) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = SpacingXXL)
                        .verticalScroll(verticalScroll)
                    ) {
                        EmploymentDetails(employer = employment.data.employer)
                        EmploymentRespondParagraphs(employerFullName = employment.data.employer.fullName)
                    }
                }
            }

            EmploymentRespondBottomBar(
                isSaving = isSaving,
                onDeny = { openModal = true },
                onAccept = onNavigateToConsent
            )
        }
    }
}