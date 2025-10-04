package com.example.scrollbooker.ui.onboarding.shared
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CollectUserUsernameScreen(
    viewModel: CollectUserUsernameViewModel,
    onSubmit: (String) -> Unit
) {
    FormLayout(
        modifier = Modifier.padding(top = 50.dp),
        enableBottomAction = false,
        headerTitle = "",
        headLine = stringResource(R.string.usernameTitle),
        subHeadLine = stringResource(R.string.chooseUsernameDescription)
    ) {
        val searchState by viewModel.searchState.collectAsState()

        val isSaving by viewModel.isSaving.collectAsState()
        var username by remember { mutableStateOf("") }

        val isSubmittedEnabled = when {
            searchState is FeatureState.Success<*> -> {
                val response = (searchState as FeatureState.Success).data
                response.available && username == response.username
            }
            isSaving is FeatureState.Loading -> false
            else -> false
        }

        val isEnabled = isSubmittedEnabled && username.length >= 3
        val isLoading = isSaving is FeatureState.Loading

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpacingXL)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapeDefaults.Large,
                value = username,
                onValueChange = {
                    username = it
                    viewModel.searchUsername(username)
                },
                label = null,
                placeholder = {
                    Text(
                        text = stringResource(R.string.usernameTitle),
                        color = Divider
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AlternateEmail,
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceBG,
                    unfocusedContainerColor = SurfaceBG,
                    cursorColor = Primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = OnSurfaceBG.copy(alpha = 0.7f),
                    focusedTextColor = OnSurfaceBG,
                    unfocusedTextColor = OnSurfaceBG
                ),
                trailingIcon = {
                    when(searchState) {
                        is FeatureState.Loading -> {
                            CircularProgressIndicator(
                                color = Divider,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        is FeatureState.Success<*> -> {
                            val response = (searchState as FeatureState.Success).data

                            Icon(
                                imageVector = if(response.available) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if(response.available) Color.Green else Error
                            )
                        }
                        else -> Unit
                    }
                }
            )

            Spacer(Modifier.height(BasePadding))

            MainButton(
                isLoading = isLoading,
                enabled = isEnabled && !isLoading,
                onClick = { onSubmit(username) },
                title = stringResource(R.string.save),
            )
        }
    }
}