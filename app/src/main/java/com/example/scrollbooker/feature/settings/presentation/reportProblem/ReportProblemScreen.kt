package com.example.scrollbooker.feature.settings.presentation.reportProblem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.reportProblem),
        onBack = onBack
    ) {
        Text("Report a Problem Screen")
    }
}