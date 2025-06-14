package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.padding
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
import com.example.scrollbooker.components.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditFullNameScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    var newFullName by rememberSaveable { mutableStateOf(user?.fullName ?: "") }
    val state = viewModel.editState.collectAsState().value

    val checkFullName = checkLength(LocalContext.current, newFullName, minLength = 3, maxLength = 30)
    val isInputValid = checkFullName.isNullOrBlank()

    val isLoading = state == FeatureState.Loading
    val isError = state == FeatureState.Error(error = null)
    val isEnabled = newFullName != user?.fullName && isInputValid

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
                title = stringResource(R.string.name),
                modifier = Modifier.padding(horizontal = BasePadding),
                onAction = { viewModel.updateFullName(newFullName) },
                actionTitle = stringResource(R.string.save),
                isLoading = isLoading,
                isEnabled = isEnabled
            )
        }
    ) {
        EditInput(
            value = newFullName,
            onValueChange = { newFullName = it },
            placeholder = stringResource(R.string.yourName),
            isError = isError || !isInputValid,
            isEnabled = !isLoading,
            isInputValid = isInputValid,
            errorMessage = checkFullName.toString()
        )
    }
}