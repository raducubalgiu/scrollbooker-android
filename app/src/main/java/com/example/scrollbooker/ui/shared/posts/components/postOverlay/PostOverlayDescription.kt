package com.example.scrollbooker.ui.shared.posts.components.postOverlay

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun PostOverlayDescription(description: String) {
    var isDescriptionCollapsed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { isDescriptionCollapsed = !isDescriptionCollapsed }
            )
    ) {
        Text(
            modifier = Modifier.animateContentSize(),
            text = description,
            color = Color.White,
            maxLines = if(isDescriptionCollapsed) Int.MAX_VALUE else 1,
            overflow = if(isDescriptionCollapsed) TextOverflow.Visible else TextOverflow.Ellipsis
        )
    }

    Spacer(Modifier.height(SpacingM))
}