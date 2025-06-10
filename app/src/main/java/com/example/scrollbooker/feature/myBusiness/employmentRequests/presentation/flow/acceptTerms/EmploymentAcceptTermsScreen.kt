package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.acceptTerms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.core.MainButton
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
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {  }
            MainButton(
                title = stringResource(R.string.sendTheRequest),
                onClick = {}
            )
        }
    }
}