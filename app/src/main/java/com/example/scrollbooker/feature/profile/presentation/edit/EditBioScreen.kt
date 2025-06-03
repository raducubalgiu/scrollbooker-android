package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditBioScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    var bio by rememberSaveable { mutableStateOf(viewModel.user?.bio ?: "") }

    Layout(noPadding = true) {
        Header(
            title = stringResource(R.string.biography),
            onBack = onBack,
            modifier = Modifier.padding(horizontal = BasePadding)
        )

        EditInput(
            value = bio,
            singleLine = false,
            minLines = 5,
            maxLines = 5,
            onValueChange = { bio = it },
            placeholder = stringResource(R.string.yourBio)
        )
    }
}