package com.example.scrollbooker.components.inputs

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.labelMedium

@Composable
fun Input(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    inputColor: Color = Color.Transparent
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    painter = trailingIcon,
                    contentDescription = null
                )
            }
        },
        label = {
            Text(
                text = label,
                style = labelLarge
            )
        },
        textStyle = bodyLarge,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = inputColor,
            unfocusedContainerColor = inputColor,
            cursorColor = Primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Primary,
            unfocusedLabelColor = OnSurfaceBG.copy(alpha = 0.7f),
            focusedTextColor = OnSurfaceBG,
            unfocusedTextColor = OnSurfaceBG
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(OnSurfaceBG, MaterialTheme.shapes.medium)
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun InputPreview() {
    ScrollBookerTheme() {
        Input(
            value = "",
            placeholder = "",
            onValueChange = {}
        )
    }
}