package com.example.scrollbooker.components.core.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.SurfaceBG

data class MenuItemData(
    val text: String,
    val enabled: Boolean = true,
    val leadingIcon: Painter? = null,
    val isLoading: Boolean = false,
    val color: Color = Color.Unspecified,
    val onClick: () -> Unit
)

@Composable
fun Menu(
    modifier: Modifier = Modifier,
    items: List<MenuItemData> = emptyList(),
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = SurfaceBG,
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it.text,
                            color = it.color
                        )
                    },
                    leadingIcon = {
                        it.leadingIcon?.let { icon ->
                            if(it.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(BasePadding),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    icon,
                                    tint = it.color,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    enabled = it.enabled,
                    onClick = {
                        expanded = false

                        it.onClick()
                    }
                )
            }
        }
    }
}
