package com.example.scrollbooker.components.core.inputs
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun InputCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
    headLine: String,
    headLineTextStyle: TextStyle = titleMedium,
    supportingText: String = "",
    supportingTextStyle: TextStyle = bodySmall,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        headlineContent = {
            Text(
                style = headLineTextStyle,
                fontWeight = FontWeight.SemiBold,
                text = headLine
            )},
        supportingContent = {
            if(supportingText.isNotEmpty()) {
                Text(
                    style = supportingTextStyle,
                    text = supportingText
                )
            }
        },
        trailingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = isEnabled,
                colors = CheckboxColors(
                    checkedCheckmarkColor = Color.White,
                    uncheckedCheckmarkColor = Color.Transparent,
                    checkedBoxColor = Primary,
                    uncheckedBoxColor = Color.Transparent,
                    disabledCheckedBoxColor = Divider,
                    disabledUncheckedBoxColor = Divider,
                    disabledIndeterminateBoxColor = Divider,
                    checkedBorderColor = Primary,
                    uncheckedBorderColor = Color.Gray,
                    disabledBorderColor = Divider,
                    disabledUncheckedBorderColor = Divider,
                    disabledIndeterminateBorderColor = Divider
                )
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = SurfaceBG
        )

    )
}