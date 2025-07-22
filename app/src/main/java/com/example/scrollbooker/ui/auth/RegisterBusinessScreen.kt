package com.example.scrollbooker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkEmail
import com.example.scrollbooker.core.util.checkPassword
import com.example.scrollbooker.ui.auth.components.AuthErrorMessage
import com.example.scrollbooker.ui.auth.components.AuthHeader
import com.example.scrollbooker.ui.auth.components.PasswordRequirement
import com.example.scrollbooker.ui.theme.Background

@Composable
fun RegisterBusinessScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onSubmit: (email: String, password: String) -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is FeatureState.Loading
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var wasSubmitted by remember { mutableStateOf(false) }

    val emailError = if(wasSubmitted) checkEmail(email) else null
    val isValidPassword = checkPassword(password)

    val isValid = wasSubmitted && emailError == null
    val isEnabled = email.isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {
        Header(
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(
                    top = 20.dp,
                    bottom = BasePadding,
                    start = SpacingXL,
                    end = SpacingXL
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                AuthHeader(
                    headLine = stringResource(R.string.registerBusiness),
                    subHeadline = stringResource(R.string.registerBusinessDescription)
                )

                Input(
                    value = email,
                    onValueChange = {
                        email = it
                        if(wasSubmitted) wasSubmitted = false
                    },
                    label = stringResource(R.string.email),
                    isError = emailError != null
                )

                emailError?.let { AuthErrorMessage(errorMessage = emailError) }

                Spacer(Modifier.height(SpacingS))

                Input(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    isError = wasSubmitted && !isValidPassword,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                if(wasSubmitted && !isValidPassword) {
                    Spacer(Modifier.height(SpacingS))
                    PasswordRequirement(password)
                }

                MainButton(
                    modifier = Modifier.padding(vertical = BasePadding),
                    isLoading = isLoading,
                    enabled = !isLoading && isEnabled,
                    title = stringResource(R.string.register),
                    onClick = {
                        wasSubmitted = true
                        if(isValid) {
                            onSubmit(email, password)
                        }
                    }
                )
            }
        }
    }
}