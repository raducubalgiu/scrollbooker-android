package com.example.scrollbooker.ui.settings.account
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun AccountScreen(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(id = R.string.account),
        onBack = onBack
    ) {
        Text("Account Screen")
    }
}