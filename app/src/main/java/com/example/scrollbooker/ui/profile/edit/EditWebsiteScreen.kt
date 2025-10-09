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
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun EditWebsiteScreen(
    viewModel: MyProfileViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    var newWebsite by rememberSaveable { mutableStateOf(user?.website ?: "") }
    val state = viewModel.editState.collectAsState().value

    val checkWebsite = checkLength(LocalContext.current, newWebsite, minLength = 3, maxLength = 255)
    val isInputValid = checkWebsite.isNullOrBlank()

    val isLoading = state == FeatureState.Loading
    val isError = state == FeatureState.Error(error = null)
    val isEnabled = newWebsite != user?.website && isInputValid

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
                title = stringResource(R.string.website),
                onAction = { viewModel.updateWebsite(newWebsite) },
                isLoading = isLoading,
                isEnabled = isEnabled
            )
        }
    ) {
        EditInput(
            value = newWebsite,
            onValueChange = { newWebsite = it },
            placeholder = stringResource(R.string.website),
            isError = isError || !isInputValid,
            isEnabled = !isLoading,
            isInputValid = isInputValid,
            errorMessage = checkWebsite.toString()
        )
    }
}