package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun OwnClientService(
    options: List<Option>,
    isServicesSelectEnabled: Boolean,
    selectedServiceId: String,
    onSelectedServiceId: (String?) -> Unit,
    serviceName: String,
    onServiceChange: (String) -> Unit,
    isServiceNameValid: Boolean,
    serviceNameError: String?,
    serviceNameMaxLength: Int?
) {
    var shouldSelectService by rememberSaveable { mutableStateOf(true) }

    val isNotValidSelect = selectedServiceId.isBlank() || selectedServiceId == "null"

    Column {
        Row {
            Column(Modifier.weight(0.8f)) {
                AnimatedContent(
                    targetState = shouldSelectService,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { target ->
                    when(target) {
                        true -> {
                            Column {
                                InputSelect(
                                    label = "${stringResource(R.string.service)}*",
                                    placeholder = "${stringResource(R.string.service)}*",
                                    isEnabled = isServicesSelectEnabled,
                                    options = options,
                                    selectedOption = selectedServiceId,
                                    onValueChange = onSelectedServiceId,
                                    shouldDisplayRequiredMessage = false
                                )
                                AnimatedVisibility(visible = isNotValidSelect) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        false -> {
                            Input(
                                modifier = Modifier.weight(0.8f),
                                label = "${stringResource(R.string.service)}*",
                                value = serviceName,
                                onValueChange = onServiceChange,
                                isError = !isServiceNameValid,
                                maxLength = serviceNameMaxLength
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(SpacingS))

            CustomIconButton(
                imageVector = if(shouldSelectService) Icons.Default.Edit
                              else Icons.Default.FormatListNumbered,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SurfaceBG),
                onClick = { shouldSelectService = !shouldSelectService },
                tint = Color.Gray,
            )
        }

        when (shouldSelectService) {
            true -> {
                AnimatedVisibility(visible = isNotValidSelect) {
                    ServiceErrorMessage(errorMessage = stringResource(R.string.requiredValidationMessage))
                }
            }
            false -> {
                AnimatedVisibility(visible =
                    !isServiceNameValid &&
                            serviceNameError?.isNotBlank() == true
                ) { ServiceErrorMessage(errorMessage = serviceNameError ?: "") }
            }
        }
    }
}

@Composable
private fun ServiceErrorMessage(errorMessage: String) {
    Column(modifier = Modifier.padding(top = BasePadding)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = Error
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = errorMessage,
                color = Error,
                style = bodyMedium
            )
        }
    }
}