package com.example.scrollbooker.ui.shared.post.components
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
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun PostShimmer() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(SpacingS),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.3f)
                .height(20.dp)
                .clip(shape = ShapeDefaults.Medium)
                .shimmerEffect(mode = ShimmerMode.DARK)
            )

            Spacer(Modifier.height(SpacingM))

            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(20.dp)
                .clip(shape = ShapeDefaults.Medium)
                .shimmerEffect(mode = ShimmerMode.DARK)
            )

            Spacer(Modifier.height(BasePadding))

            Spacer(modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .height(35.dp)
                .clip(shape = ShapeDefaults.Medium)
                .shimmerEffect(mode = ShimmerMode.DARK)
            )

            Spacer(Modifier.height(BasePadding))
        }
    }
}