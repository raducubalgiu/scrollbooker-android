package com.example.scrollbooker.screens.profile.edit
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.screens.profile.myProfile.ProfileSharedViewModel

@Composable
fun EditBioScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    var newBio by rememberSaveable { mutableStateOf(user?.bio ?: "") }
    val state = viewModel.editState.collectAsState().value

    val checkBio = checkLength(LocalContext.current, newBio, maxLength = 100)
    val isInputValid = checkBio.isNullOrBlank()

    val isLoading = state == FeatureState.Loading
    val isError = state == FeatureState.Error(error = null)
    val isEnabled = newBio != user?.bio && isInputValid

    if(viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        header = {
            HeaderEdit(
                onBack = onBack,
                title = stringResource(R.string.biography),
                modifier = Modifier.padding(horizontal = BasePadding),
                onAction = { viewModel.updateBio(newBio) },
                actionTitle = stringResource(R.string.save),
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
            isError = isError,
            isEnabled = !isLoading,
            isInputValid = isInputValid,
            errorMessage = checkBio.toString()
        )
    }
}