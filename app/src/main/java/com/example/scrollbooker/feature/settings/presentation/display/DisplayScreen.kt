package com.example.scrollbooker.feature.settings.presentation.display
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout

@Composable
fun DisplayScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.display),
        onBack = onBack
    ) {
        Text("Display Screen")
    }
}