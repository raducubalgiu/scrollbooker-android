package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.HeaderEdit
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditUsernameScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    var newUsername by rememberSaveable { mutableStateOf(viewModel.user?.username ?: "") }
    val state = viewModel.editState.collectAsState().value
    val isLoading = state == FeatureState.Loading
    val isEnabled = isLoading || newUsername != viewModel.user?.username

    if(viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    Layout {
        HeaderEdit(
            onBack = onBack,
            title = stringResource(R.string.username),
            modifier = Modifier.padding(horizontal = BasePadding),
            onAction = { viewModel.updateUsername(newUsername) },
            actionTitle = stringResource(R.string.save),
            isLoading = isLoading,
            isEnabled = isEnabled
        )

        Column(Modifier.padding(BasePadding)) {
            EditInput(
                value = newUsername,
                onValueChange = { newUsername = it },
                placeholder = stringResource(R.string.username),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AlternateEmail,
                        contentDescription = null
                    )
                }
            )
        }
    }
}