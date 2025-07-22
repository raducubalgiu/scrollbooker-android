package com.example.scrollbooker.modules.posts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.ShimmerMode
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun PostShimmer() {
    val brush = rememberShimmerBrush(mode = ShimmerMode.DARK)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(SpacingS),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .height(40.dp)
                .clip(shape = ShapeDefaults.Medium)
                .background(brush)
            )
        }
    }
}