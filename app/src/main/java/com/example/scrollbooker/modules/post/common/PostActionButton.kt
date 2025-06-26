package com.example.scrollbooker.modules.post.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS

@Composable
fun PostActionButton(
    isAction: Boolean,
    tint: Color = Color.White,
    counter: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var count by remember { mutableIntStateOf(counter) }

    Box(modifier = Modifier
        .width(65.dp)
        .clickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.padding(vertical = SpacingS),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(35.dp),
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
            Spacer(Modifier.height(SpacingXXS))
            Text(
                color = Color.White,
                text = formatCounters(count),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    ),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}