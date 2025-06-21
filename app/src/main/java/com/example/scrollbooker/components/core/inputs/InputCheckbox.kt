package com.example.scrollbooker.components.core.inputs
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun InputCheckbox(
    modifier: Modifier = Modifier,
    background: Color = SurfaceBG,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
    headLine: String,
    headLineTextStyle: TextStyle = bodyLarge,
    supportingText: String = "",
    supportingTextStyle: TextStyle = bodySmall,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable(onClick = { onCheckedChange(checked) })
            .then(modifier),
        headlineContent = {
            Text(
                modifier = Modifier.padding(start = SpacingM),
                style = headLineTextStyle,
                text = headLine,
                color = OnBackground
            )},
        supportingContent = {
            if(supportingText.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(start = SpacingM),
                    style = supportingTextStyle,
                    text = supportingText
                )
            }
        },
        trailingContent = {
            Checkbox(
                modifier = Modifier.padding(end = SpacingM).height(70.dp),
                checked = checked,
                onCheckedChange = null,
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
            containerColor = background
        )

    )
}