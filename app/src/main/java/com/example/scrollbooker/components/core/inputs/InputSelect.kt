package com.example.scrollbooker.components.core.inputs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun InputSelect(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    options: List<Option>,
    selectedOption: String,
    onValueChange: (String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var parentWidth by remember { mutableStateOf(0) }

    val rotation by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f,
        label = "ArrowRotation"
    )

    val selected = options.find { it.value == selectedOption }

    Box(modifier = modifier
        .clip(MaterialTheme.shapes.medium)
        .onGloballyPositioned { coordinates ->
            parentWidth = coordinates.size.width
        }
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = selected?.name ?: placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            },
            modifier = Modifier
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(SurfaceBG)
                .width(with(LocalDensity.current) { parentWidth.toDp() })
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        onValueChange(option.value)
                        expanded = false
                    }
                )
            }
        }
    }
}

data class Option(
    val value: String?,
    val name: String
)