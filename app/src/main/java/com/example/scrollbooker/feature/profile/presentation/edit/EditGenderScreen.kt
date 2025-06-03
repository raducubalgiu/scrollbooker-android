package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditGenderScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    Layout(noPadding = true) {
        Header(
            title = "Sex",
            onBack = onBack,
            modifier = Modifier.padding(horizontal = BasePadding)
        )
    }
}