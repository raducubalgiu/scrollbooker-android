package com.example.scrollbooker.components.core.inputs
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun EditInput(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    isInputValid: Boolean = true,
    errorMessage: String = ""
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if(singleLine && value.isNotEmpty()) {
                Box(modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { if(isEnabled) onValueChange("") }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_circle_solid),
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.7f)
                    )
                }
            }
        },
        label = null,
        placeholder = { Text(
            text = placeholder,
            color = Divider
        ) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = if(isError) Error.copy(alpha = 0.5f) else Primary,
            focusedIndicatorColor = if(isError) Error.copy(alpha = 0.5f) else Divider,
            unfocusedIndicatorColor = if(isError) Error.copy(alpha = 0.5f) else Divider,
            focusedLabelColor = if(isError) Error.copy(alpha = 0.5f) else Divider,
            unfocusedLabelColor = Color.Transparent,
            focusedTextColor = OnBackground,
            unfocusedTextColor = OnBackground,
            disabledContainerColor = Color.Transparent
        ),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        enabled = isEnabled
    )

    if(!isInputValid) {
        Column(modifier = Modifier.padding(vertical = BasePadding)) {
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
}