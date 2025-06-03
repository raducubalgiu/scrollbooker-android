package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Column
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
fun EditFullNameScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf(viewModel.user?.fullName ?: "") }

    Layout(noPadding = true) {
        Header(
            onBack = onBack,
            title = stringResource(R.string.name),
            modifier = Modifier.padding(horizontal = BasePadding)
        )

        Column(Modifier.padding(BasePadding)) {
            EditInput(
                value = name,
                onValueChange = { name = it },
                placeholder = stringResource(R.string.yourName)
            )
        }
    }
}