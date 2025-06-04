package com.example.scrollbooker.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

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
    leadingIcon: (@Composable () -> Unit)? = null
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Column(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Divider)
                    .clickable(onClick = { if(isEnabled) onValueChange("") }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = OnPrimary,

                )
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
}