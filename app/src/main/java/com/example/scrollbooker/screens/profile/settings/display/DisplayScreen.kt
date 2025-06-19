package com.example.scrollbooker.screens.profile.settings.display
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.store.theme.ThemeViewModel
import com.example.scrollbooker.store.util.ThemePreferenceEnum
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun DisplayScreen(
    onBack: () -> Unit
) {
    val viewModel: ThemeViewModel = hiltViewModel()
    val selectedTheme by viewModel.themePreferences.collectAsState()

    Layout(
        headerTitle = stringResource(R.string.applicationTheme),
        onBack = onBack
    ) {
        Column(modifier = Modifier.selectableGroup()) {
            ThemePreferenceEnum.entries.forEach { theme ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(SurfaceBG)
                        .selectable(
                            selected = (selectedTheme == theme),
                            onClick = { viewModel.updateThemePreference(theme) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = BasePadding),
                        text = theme.name,
                        style = bodyLarge,
                    )
                    RadioButton(
                        modifier = Modifier.scale(1.3f).padding(end = BasePadding),
                        selected = (selectedTheme == theme),
                        onClick = null
                    )
                }
            }
        }
    }
}