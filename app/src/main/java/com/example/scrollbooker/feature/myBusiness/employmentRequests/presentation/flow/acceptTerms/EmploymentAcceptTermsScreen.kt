package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.acceptTerms

import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list.EmploymentRequestsViewModel

@Composable
fun EmploymentAcceptTermsScreen(
    globalViewModel: EmploymentRequestsViewModel,
    localViewModel: EmploymentAcceptTermsViewModel,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = "Termeni",
        onBack = onBack
    ) {

    }
}