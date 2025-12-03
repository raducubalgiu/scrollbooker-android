package com.example.scrollbooker.components.core.inputs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun InputRadio(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: () -> Unit,
    headLine: String,
    containerColor: Color = Background,
    contentColor: Color = OnBackground,
    paddingHorizontal: Dp = SpacingXXL,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(containerColor)
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = paddingHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(leadingIcon != null) {
                leadingIcon()
                Spacer(Modifier.width(SpacingS))
            }

            Text(
                text = headLine,
                style = bodyLarge,
                color = contentColor,
            )
        }
        RadioButton(
            modifier = Modifier
                .scale(1.3f)
                .padding(end = paddingHorizontal),
            selected = selected,
            onClick = null,
            colors = RadioButtonColors(
                selectedColor = Primary,
                unselectedColor = Divider,
                disabledSelectedColor = Divider,
                disabledUnselectedColor = Divider
            )
        )
    }
}