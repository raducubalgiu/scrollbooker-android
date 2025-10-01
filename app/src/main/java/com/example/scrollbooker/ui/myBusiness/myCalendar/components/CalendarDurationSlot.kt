package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun CalendarDurationSlot(
    label: String,
    options: List<Option>,
    selectedSlot: String,
    onSlotChange: (String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var parentWidth by remember { mutableIntStateOf(0) }

    val rotation by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f,
        label = "ArrowRotation"
    )

    val selected = options.find { it.value == selectedSlot }

    Column(
        modifier = Modifier
            .padding(BasePadding)
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .clickable { expanded = true }
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                parentWidth = coordinates.size.width
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = BasePadding,
                        vertical = SpacingS
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(0.5f)) {
                    Text(
                        text = label,
                        style = labelLarge,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = selected?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = OnSurfaceBG,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .clip(shape = ShapeDefaults.Medium)
                    .background(SurfaceBG)
                    .width(with(LocalDensity.current) { parentWidth.toDp() })
            ) {
                options.forEach { option ->
                    val isSelected = selected?.value == option.value

                    DropdownMenuItem(
                        modifier = Modifier.background(
                            if(isSelected) Primary.copy(alpha = 0.55f) else Color.Transparent),
                        text = {
                            Text(
                                text = option.name.toString(),
                                color = if(isSelected) OnPrimary else OnSurfaceBG
                            )
                        },
                        onClick = {
                            onSlotChange(option.value)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}