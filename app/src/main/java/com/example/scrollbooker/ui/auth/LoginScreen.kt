package com.example.scrollbooker.ui.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkPassword
import com.example.scrollbooker.ui.auth.components.AuthBodyNavigation
import com.example.scrollbooker.ui.auth.components.AuthFooterBusinessNavigation
import com.example.scrollbooker.ui.auth.components.AuthHeader
import com.example.scrollbooker.ui.auth.components.PasswordRequirement
import com.example.scrollbooker.ui.theme.Background

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegisterClient: () -> Unit,
    onNavigateToRegisterBusiness: () -> Unit,
    onSubmit: (username: String, password: String) -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is FeatureState.Loading
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var wasSubmitted by remember { mutableStateOf(false) }
    val isValidPassword = checkPassword(password)
    val isEnabled = username.isNotEmpty() && password.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(
                    top = 50.dp,
                    bottom = BasePadding,
                    start = SpacingXL,
                    end = SpacingXL
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                AuthHeader(
                    headLine = stringResource(R.string.login),
                    subHeadline = stringResource(R.string.loginMessage)
                )

                Input(
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.username)
                )

                Spacer(Modifier.height(SpacingS))

                Input(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    isError = wasSubmitted && !isValidPassword,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
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
                    title = stringResource(id = R.string.login),
                    onClick = {
                        if(isValidPassword) {
                            onSubmit(username, password)
                            focusManager.clearFocus()
                        }
                        wasSubmitted = true
                    }
                )

                AuthBodyNavigation(
                    onNavigate = onNavigateToRegisterClient,
                    description = stringResource(R.string.dontHaveAnAccount),
                    action = stringResource(R.string.register)
                )
            }

            AuthFooterBusinessNavigation(onNavigateToRegisterBusiness)
        }
    }
}