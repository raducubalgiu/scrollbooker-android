package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.selectEmployee

import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list.EmploymentRequestsViewModel

@Composable
fun EmploymentSelectEmployeeScreen(
    globalViewModel: EmploymentRequestsViewModel,
    localViewModel: EmploymentSelectEmployeeViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = "Selecteaza angajat",
        onBack = onBack
    ) {

    }
}