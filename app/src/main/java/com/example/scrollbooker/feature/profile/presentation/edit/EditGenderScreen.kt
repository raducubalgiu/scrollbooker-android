package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditGenderScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.gender),
        onBack = onBack,
    ) {  }
}