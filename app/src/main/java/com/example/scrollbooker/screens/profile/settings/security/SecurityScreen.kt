package com.example.scrollbooker.screens.profile.settings.security
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun SecurityScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.security),
        enablePaddingH = false,
        onBack = onBack
    ) {
        Text("Security Screen")
    }
}