package com.example.scrollbooker.ui.profile.edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun EditBioScreen(
    viewModel: MyProfileViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    var newBio by rememberSaveable { mutableStateOf(user?.bio ?: "") }
    val state = viewModel.editState.collectAsState().value

    val maxLength = 160

    val checkBio = checkLength(LocalContext.current, newBio, maxLength = maxLength)
    val isInputValid = checkBio.isNullOrBlank()

    val isLoading = state == FeatureState.Loading
    val isEnabled = newBio != user?.bio && isInputValid

    if(viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    Layout(
        header = {
            HeaderEdit(
                onBack = onBack,
                title = stringResource(R.string.biography),
                onAction = { viewModel.updateBio(newBio) },
                isLoading = isLoading,
                isEnabled = isEnabled
            )
        }
    ) {
        EditInput(
            value = newBio,
            onValueChange = { newBio = it },
            placeholder = stringResource(R.string.yourBio),
            singleLine = false,
            minLines = 5,
            maxLines = 5,
            isError = !isInputValid,
            isEnabled = !isLoading,
            errorMessage = checkBio,
            maxLength = maxLength
        )
    }
}