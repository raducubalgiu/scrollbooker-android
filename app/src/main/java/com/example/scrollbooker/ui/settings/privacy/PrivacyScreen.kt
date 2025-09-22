package com.example.scrollbooker.ui.settings.privacy
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun PrivacyScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.privacy),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Text("Privacy Screen")
    }
}