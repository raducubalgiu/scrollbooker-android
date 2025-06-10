package com.example.scrollbooker.feature.myBusiness.employees.presentation
import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout

@Composable
fun EmployeesDismissalScreen(
    viewModel: EmployeesDismissalViewModel,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = "Concediere",
        onBack = onBack
    ) {

    }
}