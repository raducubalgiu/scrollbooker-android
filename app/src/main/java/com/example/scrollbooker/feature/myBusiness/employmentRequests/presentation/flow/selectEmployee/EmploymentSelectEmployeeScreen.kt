package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.selectEmployee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
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
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {  }
            MainButton(
                title = stringResource(R.string.nextStep),
                onClick = onNext
            )
        }
    }
}