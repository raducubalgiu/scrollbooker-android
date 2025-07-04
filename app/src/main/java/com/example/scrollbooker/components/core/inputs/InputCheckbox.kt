package com.example.scrollbooker.components.core.inputs
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun InputCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    isEnabled: Boolean = true,
    headLine: String,
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Background)
            .clickable { if(isEnabled) onCheckedChange() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = SpacingXXL),
            text = headLine,
            style = bodyLarge,
            color = OnBackground
        )
        Checkbox(
            modifier = Modifier.padding(end = SpacingXXL),
            checked = checked,
            onCheckedChange = null,
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
    }
}