package com.example.scrollbooker.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun Input(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
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
        Input(value = "", onValueChange = {})
    }
}