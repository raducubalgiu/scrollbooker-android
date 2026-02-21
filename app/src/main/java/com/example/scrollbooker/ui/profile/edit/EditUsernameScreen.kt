package com.example.scrollbooker.ui.profile.edit
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
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun EditUsernameScreen(
    viewModel: MyProfileViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.profile.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    var newUsername by rememberSaveable { mutableStateOf(user?.username ?: "") }
    val state = viewModel.editState.collectAsState().value
    val isLoading = state == FeatureState.Loading
    val isEnabled = isLoading || newUsername != user?.username

    if (viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    Layout(
        header = {
            HeaderEdit(
                onBack = onBack,
                title = stringResource(R.string.username),
                onAction = { viewModel.updateUsername(newUsername) },
                actionTitle = stringResource(R.string.save),
                isLoading = isLoading,
                isEnabled = isEnabled
            )
        }
    ) {
        EditInput(
            value = newUsername,
            onValueChange = { newUsername = it },
            placeholder = stringResource(R.string.username),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.AlternateEmail,
//                    contentDescription = null
//                )
//            }
        )
    }
}