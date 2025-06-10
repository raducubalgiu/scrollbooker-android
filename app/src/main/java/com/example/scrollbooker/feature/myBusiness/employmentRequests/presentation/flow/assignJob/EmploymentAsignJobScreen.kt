package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.assignJob

import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list.EmploymentRequestsViewModel

@Composable
fun EmploymentAssignJobScreen(
    globalViewModel: EmploymentRequestsViewModel,
    localViewModel: EmploymentAssignJobViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = "Ataseaza job",
        onBack = onBack
    ) {

    }
}