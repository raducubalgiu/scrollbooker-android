package com.example.scrollbooker.components.core.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    isError: Boolean = false,
    isEnabled: Boolean = true,
    inputColor: Color = SurfaceBG,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    maxLength: Int? = null,
    errorMessage: String? = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = inputColor,
        unfocusedContainerColor = inputColor,
        cursorColor = LastMinute,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = LastMinute,
        unfocusedLabelColor = Color.Gray,
        focusedTextColor = OnSurfaceBG,
        unfocusedTextColor = OnSurfaceBG,
        errorContainerColor = SurfaceBG,
    )
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (maxLength != null) {
                val isDeleting = newValue.length < value.length
                if (newValue.length <= maxLength || isDeleting) {
                    onValueChange(newValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        readOnly = readOnly,
        textStyle = bodyLarge.copy(color = if (isError) Error else OnSurfaceBG), // Ne asigurăm că respectă stilul tău
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = isEnabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = remember { MutableInteractionSource() },
                label = if (label.isNotEmpty()) {
                    { Text(text = label, style = labelLarge) }
                } else {
                    null
                },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                shape = MaterialTheme.shapes.medium,
                colors = colors,
                isError = isError,
                contentPadding = contentPadding,
                container = {
                    Surface(
                        color = if (isError) colors.errorContainerColor else colors.unfocusedContainerColor,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxSize()
                    ) {}
                }
            )
        }
    )

    AnimatedVisibility(visible = isError && errorMessage?.isNotBlank() == true) {
        Column(modifier = Modifier.padding(top = BasePadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = Error
                )
                Spacer(Modifier.width(SpacingS))
                Text(
                    text = errorMessage ?: "",
                    color = Error,
                    style = bodyMedium
                )
            }
        }
    }
}
