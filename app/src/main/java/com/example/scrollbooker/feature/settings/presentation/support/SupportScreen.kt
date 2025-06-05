package com.example.scrollbooker.feature.settings.presentation.support
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout

@Composable
fun SupportScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.support),
        onBack = onBack
    ) {
        Text("Support Screen")
    }
}