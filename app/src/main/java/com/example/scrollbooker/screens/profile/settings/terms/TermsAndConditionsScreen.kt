package com.example.scrollbooker.screens.profile.settings.terms
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.termsAndConditions),
        onBack = onBack
    ) {
        Text("Terms and Conditions")
    }
}