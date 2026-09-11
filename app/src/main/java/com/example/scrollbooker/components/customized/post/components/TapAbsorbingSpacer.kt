package com.example.scrollbooker.components.customized.post.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

// A Spacer-shaped gap that also absorbs taps. Default fillMaxWidth() needs an already width-bound parent (e.g. weight(1f)) — pass an explicit width inside a wrap-content one.
@Composable
fun TapAbsorbingSpacer(height: Dp, modifier: Modifier = Modifier.fillMaxWidth()) {
    Box(
        modifier = modifier
            .height(height)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
    )
}
