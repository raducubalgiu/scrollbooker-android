package com.example.scrollbooker.feature.settings.presentation.display
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.inputs.InputRadio
import com.example.scrollbooker.components.inputs.Option

@Composable
fun DisplayScreen(
    onBack: () -> Unit
) {
    val options = listOf(
        Option(value = "standard", name = stringResource(R.string.deviceSettings)),
        Option(value = "light", name = "Light"),
        Option(value = "dark", name = "Dark")
    )
    var theme by remember { mutableStateOf("standard") }

    Layout(
        headerTitle = stringResource(R.string.applicationTheme),
        onBack = onBack
    ) {
        InputRadio(
            value = theme,
            options = options,
            onValueChange = { theme = it }
        )
    }
}