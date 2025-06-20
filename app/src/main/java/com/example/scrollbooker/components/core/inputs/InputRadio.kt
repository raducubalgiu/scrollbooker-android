package com.example.scrollbooker.components.core.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun InputRadio(
    background: Color = SurfaceBG,
    modifier: Modifier = Modifier,
    options: List<Option>,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier
        .selectableGroup()
    ) {
        options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(background)
                    .selectable(
                        selected = (option.value == value),
                        onClick = { onValueChange(option.value.toString()) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = SpacingXXL),
                    text = option.name,
                    style = bodyLarge,
                    color = OnBackground
                )
                RadioButton(
                    modifier = Modifier.scale(1.3f).padding(end = SpacingXXL),
                    selected = (option.value == value),
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
    }
}