package com.example.scrollbooker.ui.profile.myProfile.settings.display
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.store.theme.ThemeViewModel
import com.example.scrollbooker.store.util.ThemePreferenceEnum
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun DisplayScreen(
    onBack: () -> Unit
) {
    val viewModel: ThemeViewModel = hiltViewModel()
    val selectedTheme by viewModel.themePreferences.collectAsState()

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.applicationTheme),
        onBack = onBack,
        enablePaddingH = false
    ) {
        LazyColumn {
            itemsIndexed(ThemePreferenceEnum.entries) { index, theme ->
                InputRadio(
                    headLine = theme.name,
                    selected = selectedTheme == theme,
                    onSelect = { viewModel.updateThemePreference(theme) }
                )

                if(index <= ThemePreferenceEnum.entries.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingXXL)
                            .height(0.55.dp)
                            .background(Divider.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}