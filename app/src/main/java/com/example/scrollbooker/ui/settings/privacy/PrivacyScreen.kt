package com.example.scrollbooker.ui.settings.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun PrivacyScreen(
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
    }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.privacy),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Text("Privacy Screen")
    }
}