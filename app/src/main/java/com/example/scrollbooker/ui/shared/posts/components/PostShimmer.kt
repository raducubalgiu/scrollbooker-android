package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.ShimmerMode
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun PostShimmer(
    paddingBottom: Dp = 90.dp
) {
    val brush = rememberShimmerBrush(mode = ShimmerMode.DARK)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(SpacingS)
        .padding(bottom = paddingBottom),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.3f)
                .height(20.dp)
                .clip(shape = ShapeDefaults.Medium)
                .background(brush)
            )

            Spacer(Modifier.height(SpacingM))

            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(20.dp)
                .clip(shape = ShapeDefaults.Medium)
                .background(brush)
            )

            Spacer(Modifier.height(BasePadding))

            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .height(35.dp)
                .clip(shape = ShapeDefaults.Medium)
                .background(brush)
            )

            Spacer(Modifier.height(BasePadding))
        }
    }
}