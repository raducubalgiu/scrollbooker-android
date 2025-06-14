package com.example.scrollbooker.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun BoxIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    contentAlignment: Alignment = Alignment.Center,
    iconSize: Dp = 30.dp,
    tint: Color = OnBackground
) {
    Box(modifier = Modifier
        .clickable(onClick = onClick)
        .then(modifier),
        contentAlignment = contentAlignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .padding(
                    horizontal = BasePadding,
                    vertical = SpacingM
                )
                .size(iconSize)
        )
    }
}