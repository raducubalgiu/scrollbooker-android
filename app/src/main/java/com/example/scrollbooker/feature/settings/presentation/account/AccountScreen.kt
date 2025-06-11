package com.example.scrollbooker.feature.settings.presentation.account
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun AccountScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(id = R.string.account),
        onBack = onBack
    ) {
        Text("Account Screen")
    }
}