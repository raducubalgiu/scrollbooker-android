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
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
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
    val isSaving by viewModel.isSaving.collectAsState()
    val verticalScroll = rememberScrollState()

    LaunchedEffect(employmentId) {
        if(employmentId != null) {
            viewModel.setEmploymentId(employmentId)
        }
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
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = SpacingXXL)
                .verticalScroll(verticalScroll)
            ) {
                EmploymentDetails()
                EmploymentRespondParagraphs()
            }

            EmploymentRespondBottomBar(
                isSaving = isSaving,
                onDeny = { onDenyEmployment(EmploymentRequestStatusEnum.DENIED) },
                onAccept = onNavigateToConsent
            )
        }
    }
}