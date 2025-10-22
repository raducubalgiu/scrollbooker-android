package com.example.scrollbooker.components.core.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MainButtonOutlined(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    shape: Shape = ButtonDefaults.outlinedShape,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    icon: Painter? = null,
    iconColor: Color = Color.Black,
    trailingIcon: ImageVector = Icons.Default.KeyboardArrowDown,
    trailingIconTint: Color = OnBackground,
    showTrailingIcon: Boolean = false
) {
    OutlinedButton(
        modifier = modifier,
        contentPadding = contentPadding,
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = OnBackground
        ),
        border = BorderStroke(1.dp, Divider),
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconColor
                )
                Spacer(Modifier.width(SpacingS))
            }
            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(15.dp),
                    strokeWidth = 4.dp,
                    color = Divider
                )
            } else {
                Text(
                    text = title,
                    style = titleMedium
                )
            }

            if(showTrailingIcon) {
                Spacer(Modifier.width(SpacingS))

                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = trailingIconTint
                )
            }
        }
    }
}