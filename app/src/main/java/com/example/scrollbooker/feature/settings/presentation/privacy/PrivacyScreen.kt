package com.example.scrollbooker.feature.settings.presentation.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout

@Composable
fun PrivacyScreen(
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
    }

    Layout(
        headerTitle = stringResource(R.string.privacy),
        onBack = onBack,
        enablePadding = false
    ) {
        Text("Privacy Screen")
    }
}