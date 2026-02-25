package com.example.scrollbooker.components.core.inputs
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun Input(
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
    TextField(
        value = value,
        onValueChange = { newValue ->
            if(maxLength != null) {
                val isDeleting = newValue.length < value.length
                if(newValue.length <= maxLength || isDeleting) {
                    onValueChange(newValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        label = {
            Text(
                text = label,
                style = labelLarge
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        textStyle = bodyLarge,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = isEnabled,
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = OnSurfaceBG,
                shape = ShapeDefaults.Medium
            ),
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