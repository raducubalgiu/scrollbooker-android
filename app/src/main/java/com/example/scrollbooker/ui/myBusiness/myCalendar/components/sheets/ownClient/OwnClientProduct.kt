package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient

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
fun OwnClientProduct(
    options: List<Option>,
    isProductsSelectEnabled: Boolean,
    selectedProductId: String,
    onSelectedProductId: (String?) -> Unit,
    productName: String,
    onProductChange: (String) -> Unit,
    isProductNameValid: Boolean,
    productNameError: String?,
    productNameMaxLength: Int?
) {
    var shouldSelectProduct by rememberSaveable { mutableStateOf(true) }

    val isNotValidSelect = selectedProductId.isBlank() || selectedProductId == "null"

    Column {
        Row {
            Column(Modifier.weight(0.8f)) {
                AnimatedContent(
                    targetState = shouldSelectProduct,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { target ->
                    when(target) {
                        true -> {
                            Column {
                                InputSelect(
                                    label = "${stringResource(R.string.product)}*",
                                    placeholder = "${stringResource(R.string.product)}*",
                                    isEnabled = isProductsSelectEnabled,
                                    options = options,
                                    selectedOption = selectedProductId,
                                    onValueChange = onSelectedProductId,
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
                                label = "${stringResource(R.string.product)}*",
                                value = productName,
                                onValueChange = onProductChange,
                                isError = !isProductNameValid,
                                maxLength = productNameMaxLength
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(SpacingS))

            CustomIconButton(
                imageVector = if(shouldSelectProduct) Icons.Default.Edit else Icons.Default.FormatListNumbered,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SurfaceBG),
                onClick = { shouldSelectProduct = !shouldSelectProduct },
                tint = Color.Gray,
            )
        }

        when (shouldSelectProduct) {
            true -> {
                AnimatedVisibility(visible = isNotValidSelect) {
                    ServiceErrorMessage(errorMessage = stringResource(R.string.requiredValidationMessage))
                }
            }
            false -> {
                AnimatedVisibility(visible =
                    !isProductNameValid &&
                            productNameError?.isNotBlank() == true
                ) { ServiceErrorMessage(errorMessage = productNameError ?: "") }
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