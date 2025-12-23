package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.extensions.withAlpha
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.shared.posts.util.formatCounters

@Composable
fun PostActionButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    enableOpacity: Boolean = false,
    tint: Color = Color.White,
    counter: Int? = null,
    icon: Int,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .width(65.dp)
        .clickable(
            onClick = {
                if(isEnabled) {
                    onClick?.invoke()
                }
            },
            interactionSource = interactionSource,
            indication = null
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.padding(top = BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = tint
            )
            counter?.let {
                Spacer(Modifier.height(SpacingXXS))
                Text(
                    color = Color.White.withAlpha(enableOpacity),
                    text = formatCounters(it),
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
}